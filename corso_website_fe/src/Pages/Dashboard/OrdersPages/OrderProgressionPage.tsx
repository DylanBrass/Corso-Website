import React, {useEffect, useState} from "react";
import axios from "axios";
import swal from "sweetalert";
import Order from "../../../ressources/Models/Order";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import './OrderProgressionPage.css';
import {useParams} from "react-router-dom";
import {useAuth} from "../../../security/Components/AuthProvider";
import {Bounce, toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {SyncLoader} from "react-spinners";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import '../../globalStyling/globalStyling.css';


function OrderProgressionPage() {
    const [order, setOrder] = useState<Order>(
        new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0)
    );
    //Progression is the notes
    const [notes, setNotes] = useState<string>("");
    const [allServices, setAllServices] = React.useState([]);
    const {orderId} = useParams();
    const [updatingOrder, setUpdatingOrder] = useState<boolean>(false);
    const {t} = useTranslation();
    const [loading, setLoading] = useState(false);

    const auth = useAuth()

    const completeOrder = async () => {
        if (!order.orderStatus.includes("COMPLETED")) {
            setLoading(true);
            await swal({
                title: (t("order.completeMessageQuestion")),
                text: (t("order.completeMessageQuestion2")),
                icon: "warning",
                buttons: [(t("order.completeQuestionButtonsCancel")), (t("order.completeQuestionButtonsConfirm"))],
                dangerMode: true,
            }).then(async (willComplete) => {
                if (willComplete) {


                    loadingCompletingOrder();
                    let data = {
                        orderId: orderId,
                        orderStatus: "COMPLETED",
                    };

                    try {
                        // @ts-ignore
                        const response = await axios.patch(
                            `${process.env.REACT_APP_BE_HOST}api/v1/corso/orders/manage/completedOrder/${orderId}`,
                            data,
                            {
                                headers: {
                                    // @ts-ignore
                                    "X-XSRF-TOKEN": auth.getXsrfToken(),
                                },
                            }
                        );
                        if (response.status === 200) {
                            await swal({
                                title: `${t("order.orderCompletedMessage")} ${order.orderTrackingNumber} ${t("order.orderCompletedMessage2")}`,
                                icon: "success"
                            }).then(() => {
                                setLoading(false);
                                window.location.href = `/manage/orders/inactive/${orderId}`;
                            });
                        } else {
                            setLoading(false);
                            await swal((t("order.orderCompletedErrorMessage")), "error");
                        }
                    } catch (error) {
                        console.log(orderId);
                        console.log(error);
                        console.log(order.orderStatus);

                        setLoading(false);
                        await swal(`${t("order.CompletedErrorMessage2")} ${order.orderTrackingNumber}.`, "error");
                    }
                } else {
                    setLoading(false);
                    window.location.href = `/manage/currentOrder/${orderId}`;

                }
            });
        }
    };

    //GET THE ORDER ID
    const getOrder = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/manage/${orderId}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                const orderData = res.data;
                if (orderData.orderStatus.toUpperCase() === "COMPLETED" || orderData.orderStatus.toUpperCase() === "CANCELLED" || orderData.orderStatus.toUpperCase() === "DECLINED") {
                    window.location.href = `/manage/orders/inactive/${orderId}`;
                } else if (orderData.orderStatus.toUpperCase() === "PENDING") {
                    window.location.href = `/manage/orders/${orderId}`;
                }

                setOrder(orderData);
                if (orderData.progressInformation !== null) {
                    setNotes(orderData.progressInformation);
                } else {
                    setNotes("");
                }
            })
            .catch(err => {
                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }
            });
    };

    const updateOrderProgression = async (e: any, orderId: any, progressionValue: string, hoursWorked: any) => {
            e.preventDefault();

            setUpdatingOrder(true);


            try {
                if (order.estimatedDuration < hoursWorked) {
                    throw new (Error)(t("order.errorHoursGreaterEstimatedDuration"));
                }


                loadingOrderUpdateProgression();

                let data = {
                    "progressInformation": progressionValue,
                    "hoursWorked": hoursWorked,
                    "estimatedDuration": order.estimatedDuration,
                    "serviceId": (document.getElementById("serviceId") as HTMLInputElement).value
                };

                const response = await axios.patch(
                    `${process.env.REACT_APP_BE_HOST}api/v1/corso/orders/manage/updateProgression/${orderId}`, data,
                    {
                        headers: {
                            // @ts-ignore
                            "X-XSRF-TOKEN": auth.getXsrfToken(),
                        },
                    }
                );
                console.log("API Response:", response.data);
                console.log("Progression Value:", progressionValue);
                console.log("Service Name:", order.service.serviceName);
                setOrder((prevOrder) => ({...prevOrder, progressInformation: progressionValue}));
                setNotes(progressionValue);

                swal({
                    title: t("order.updateSuccessMessage"),
                    icon: "success"
                })
                    .then(async () => {
                            if (order.estimatedDuration === hoursWorked && order.orderStatus.toLowerCase() === "in_progress"
                                && order.estimatedDuration !== 0 && order.hoursWorked !== 0
                            ) {
                                await swal({
                                    title: (t("order.orderCompletedOrderQuestion1")),
                                    icon: "info",
                                    buttons: [t("yes"), t("no")],
                                }).then(async (value) => {
                                        if (value) {
                                            setLoading(true);
                                            window.location.href = `/manage/currentOrder/${orderId}`;
                                        } else {
                                            await completeOrder().then(() => {
                                            }).catch(async () => {
                                                await swal((t("order.completeOrderError")), "error");
                                            })
                                        }
                                    }
                                )
                            } else {
                                window.location.href = `/manage/currentOrder/${orderId}`;
                            }
                        }
                    )
                ;
            } catch
                (error) {


                // @ts-ignore
                if (error.message) {
                    // @ts-ignore
                    await swal((t("error")), error.message, "error");
                    return
                }
                await swal((t("order.updateErrorMessage")), "error");

            } finally {
                setUpdatingOrder(false);

            }
        }
    ;

    const getServices = async () => {

        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/services", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    setAllServices(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }

    const handleProgressionNotesChange = (
        e: React.ChangeEvent<HTMLTextAreaElement>
    ) => {
        setNotes(e.target.value);
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const target = e.target as HTMLFormElement;
        if (target && target.name === 'updateOrderButton') {
            await updateOrderProgression(e, order.orderId, notes, order.hoursWorked);
            await getOrder();
        }
    };

    const cancelBtn = () => {
        window.location.href = `/manage/currentOrder/${order.orderId}`;
    }

    const handleEstimatedDurationChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value);
        setOrder(prevOrder => ({
            ...prevOrder,
            estimatedDuration: value
        }));
    };


    const handleHoursWorkedChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value);
        setOrder(prevOrder => ({
            ...prevOrder,
            hoursWorked: value
        }));
    }

    const handleServiceChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedServiceId = e.target.value;
        setOrder(prevOrder => ({
            ...prevOrder,
            serviceId: selectedServiceId
        }));
    };


    const loadingOrderUpdateProgression = () => {
        toast((t("order.orderUpdateLoadingProgression")), {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: false,
            pauseOnHover: true,
            draggable: false,
            bodyClassName: "toast-body",
            transition: Bounce,
        });
    };

    const loadingCompletingOrder = () => {
        toast((t("order.orderCompletedLoading")), {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: false,
            pauseOnHover: true,
            draggable: false,
            bodyClassName: "toast-body",
            transition: Bounce,
        });
    }


    useEffect(() => {
        getOrder();
        getServices();
        // eslint-disable-next-line
    }, [orderId])

    const backButtonHref = "/dashboard";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div className="container specific-order">
                <div className="row">
                    <h1 className="display-4 title text-center"
                        id={"title"}>{t("order.order")} #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 info-box">
                        <div className="row text-start">
                            <h1>{t("customerInfo.customer")}</h1>
                            <p>{order.customerFullName}</p>
                        </div>
                        <div className="row text-start overflow-scroll">
                            <h1>{t("customerInfo.address")}</h1>
                            <p>
                                {t("customerInfo.address")}
                                : {order.customerAddress} {order.customerApartmentNumber !== null ? "Apartment " + order.customerApartmentNumber : ""}, {order.customerCity}  </p>
                            <p>{t("customerInfo.postalCode")} : {order.customerPostalCode}</p>
                        </div>
                        <div className="row text-start">
                            <h1>{t("customerInfo.contactInformation")}</h1>
                            <p>{t("customerInfo.email")} : {order.customerEmail}</p>
                            {(order.customerPhone !== null && order.customerPhone !== "") &&
                                <p> {t("customerInfo.phoneNumber")} : {order.customerPhone}</p>}
                        </div>


                        <div className="row text-start">
                            <h1>Service</h1>
                            <div className="col-sm-3">

                                <select name="serviceId" id="serviceId" required onChange={handleServiceChange}
                                        value={order.serviceId}>
                                    {
                                        allServices.map((service: any) => {
                                            return (
                                                <option key={service.serviceId} value={service.serviceId}>
                                                    {getValuesFromJSON(i18n.language, service.serviceName)}
                                                </option>
                                            )
                                        })
                                    }
                                </select>

                            </div>
                        </div>

                        {(order.orderDescription !== null && order.orderDescription !== "") &&
                            <div className="row text-start">
                                <h1>Description</h1>
                                <p>{order.orderDescription}</p>
                            </div>
                        }


                        <div className="row text-start">
                            <h1>{t("order.orderStatus")}</h1>
                            <p className={"text-capitalize"}>
                                {order.orderStatus.replace("_", " ").toLowerCase()}
                            </p>
                        </div>
                    </div>
                    <div className="col-lg-6 col-md-12">
                        <div className={"row"}>

                            {/*Handle estimation duration*/}
                            <h4 className="progression-editOrderPage-title">Progression</h4>
                            <form onSubmit={handleSubmit}>
                                <div className="col-lg-12 col-md-12 p-4 progress-current-wrapper">
                                    <div className="row">
                                        <div className="col">
                                            <div className="progression-divClass">
                                                <label className="label-progression-divClass"
                                                       htmlFor="estimationWorked">
                                                    {t("order.hourWorkedEstimation")}:</label>
                                                <input
                                                    type="number"
                                                    id="estimationWorked"
                                                    name="estimationWorked"
                                                    value={order.estimatedDuration}
                                                    onChange={handleEstimatedDurationChange}
                                                    min="0"
                                                />

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>

                            <form onSubmit={handleSubmit} id="updateOrderForm">
                                <div className="col-lg-6 col-md-6">
                                    <div className="row">
                                        <div className="col">
                                            <div className="hours-worked-container">
                                                <div className="hours-container">
                                                    <label
                                                        className="hours-worked-title">{t("order.hoursWorked")}:</label>
                                                    <div className="hours-numbers-container">
                                                        <input
                                                            type="number"
                                                            className="display-hours-worked"
                                                            value={order.hoursWorked}
                                                            onChange={handleHoursWorkedChange}
                                                            min="0"
                                                        />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>

                        </div>

                        {/*HANDLES THE NOTES*/}
                        <form onSubmit={handleSubmit} id="updateOrderForm">
                            <div className={"row"}>
                                <h4 className={"notes-title"}>Notes</h4>
                                <textarea
                                    className={"notes-box overflow-scroll"}
                                    value={notes}
                                    onChange={handleProgressionNotesChange}
                                    placeholder={t("order.enterNotes")}
                                />
                            </div>
                        </form>


                        {/*CANCEL AND UPDATE BUTTONS*/}
                        <div className="updateAndCancel-orderProgression-divClass">
                            <div className={"cancelOrderBtn-divClass"}>

                                {!updatingOrder && !loading && (

                                    <button
                                        className="btn-style cancel-orderProgression-btn"
                                        type="button"
                                        disabled={updatingOrder || loading}
                                        onClick={cancelBtn}>
                                        {t("order.cancelUpdateOrder")}
                                    </button>
                                )}
                            </div>


                            <div className={"updateOrderBtn-divClass"}>
                                {!updatingOrder && !loading && (
                                    <button
                                        className="btn-style-green update-orderProgression-btn"
                                        onClick={(e) => updateOrderProgression(e, order.orderId, notes, order.hoursWorked)}
                                        name="updateOrderButton"
                                        type="submit"
                                        value="updateOrderButton"
                                        disabled={updatingOrder || loading}
                                        form="updateOrderForm">
                                        {t("order.updateOrderBtn")}
                                    </button>
                                )}
                                {updatingOrder &&

                                    <SyncLoader
                                        color="#36d7b7"
                                        size={10}
                                        className={"updateOrderBtn-syncLoadere"}
                                    />
                                }
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>


    );
}

export default OrderProgressionPage;
