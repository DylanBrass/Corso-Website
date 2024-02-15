import React, {useEffect, useState} from "react";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {ProgressBar, Step} from "react-step-progress-bar";
import check from "../../../ressources/images/check_done.png";
import checkToDo from "../../../ressources/images/check_notDone.png";
import {useNavigate} from "react-router";
import {useTranslation} from "react-i18next";
import Order from "../../../ressources/Models/Order";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import {useParams} from "react-router-dom";
import {useAuth} from "../../../security/Components/AuthProvider";
import './ViewSpecificInactiveOrder.css';
import axios from "axios";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

export default function ViewSpecificInactiveOrder() {
    const {t} = useTranslation();

    const [order, setOrder] = useState<Order>(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    let {orderId} = useParams();
    const [orderStatus, setOrderStatus] = React.useState<string>(order.orderStatus);

    const auth = useAuth()

    const navigate = useNavigate();

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
                const order = res.data;
                setOrderStatus(order.orderStatus);
                if (order.orderStatus.toLowerCase() === "pending") {
                    window.location.href = `/manage/orders/${orderId}`;
                }else if (order.orderStatus.toLowerCase() === "in_progress") {
                    window.location.href = `/manage/currentOrder/${orderId}`;
                }
                setOrder(order);
            })
            .catch(err => {


                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

            })

    }


    const deletePermantlyTheOrder = async () => {
        axios.delete(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/manage/${orderId}/permanent`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                // @ts-ignore
                swal("Order deleted", "The order has been deleted", "success").then(() => {
                    navigate(-1)
                })
            })
            .catch(err => {
                // @ts-ignore
                auth.authError(err.response.status);
            })
    }


    useEffect(() => {
        getOrder();
        // eslint-disable-next-line
    }, []);

    useEffect(() => {
        if(i18n.language === "fr") {
            switch (order.orderStatus.toLowerCase()) {
                case "completed":
                    setOrderStatus("terminée");
                    break;
                case "cancelled":
                    setOrderStatus("annulée");
                    break;
                case "declined":
                    setOrderStatus("refusée");
                    break;
            }
        }
        else {
            setOrderStatus(order.orderStatus)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [i18n.language, orderStatus]);

    const backButtonHref = "/orders/completed";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="container specific-order">
                <div className="row">
                    <h1 className="display-4 title text-center"
                        id={"title"}>{t("order.order")} #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 request-info-box">
                        <div className="row text-start">
                            <h1>Client</h1>
                            <p>{order.customerFullName}</p>
                        </div>
                        <div className="row text-start overflow-scroll">
                            <h1>{t("customerInfo.address")}</h1>
                            <p>
                                {t("customerInfo.address")}
                                : {order.customerAddress} {order.customerApartmentNumber !== null ? "Apartment " + order.customerApartmentNumber : ""}, {order.customerCity}  </p>
                            <p>{t("customerInfo.postalCode")}: {order.customerPostalCode}</p>
                        </div>
                        <div className="row text-start">
                            <h1>{t("customerInfo.contactInformation")}</h1>
                            <p>{t("customerInfo.email")}: {order.customerEmail}</p>
                            {(order.customerPhone !== null && order.customerPhone !== "") &&
                                <p> {t("customerInfo.phoneNumber")}: {order.customerPhone}</p>}
                        </div>
                        <div className="row text-start">
                            <h1>Service</h1>
                            <p>{`${getValuesFromJSON(i18n.language, order.service.serviceName)}`}</p>
                        </div>
                        {(order.orderDescription !== null && order.orderDescription !== "") &&
                            <div className="row text-start">
                                <h1>Description</h1>
                                <p>{order.orderDescription}</p>
                            </div>
                        }
                        <div className="row text-start">
                            <h1>{t("order.orderStatus")}</h1>
                            <p className={"text-capitalize"}>{
                                orderStatus.replace("_", " ").toLowerCase()
                            }</p>
                        </div>
                    </div>
                    <div className="col-lg-6 col-md-12">
                        <div className={"row"}>
                            <h4>Progression</h4>
                            <div className="col-lg-12 col-md-12 p-4">
                                {order.estimatedDuration > 0 &&
                                    <div className={"progress-bar-wrapper"}>
                                        <ProgressBar
                                            hasStepZero={true}
                                            percent={order.hoursWorked / order.estimatedDuration * 100}
                                            filledBackground="#47B2FF"
                                        >

                                            {Array.from({length: 4}, (_, index) => (
                                                <Step transition="scale" key={index}>
                                                    {({accomplished, position}) => (
                                                        <div
                                                            className={`${accomplished ? "accomplished" : null}`}>
                                                            {accomplished ? (
                                                                <img src={check} alt={"accomplished"}/>
                                                            ) : (
                                                                <img src={checkToDo} alt={"to be done"}/>
                                                            )}
                                                        </div>

                                                    )}
                                                </Step>
                                            ))
                                            }

                                        </ProgressBar>

                                        <div>
                                            <h5 className={"text-center mt-2"}>{order.hoursWorked} / {order.estimatedDuration} {t("order.hours")}</h5>
                                        </div>
                                    </div>
                                }
                                {order.estimatedDuration <= 0 &&
                                    <p>{t("order.orderNotStartedMessage")}</p>
                                }
                            </div>
                            <div className={"row"}>
                                <h4 className={"notes-title"}>{t("order.electricianNotesTitle")}</h4>
                                <div className={"notes-box overflow-scroll"}>
                                    {order.progressInformation !== null && order.progressInformation.length > 0 &&
                                        <p>{order.progressInformation}</p>
                                    }
                                    {
                                        (order.progressInformation === null || order.progressInformation.length < 0) &&
                                        <p>{t("order.noNotesYetMessage")}</p>

                                    }
                                </div>
                            </div>
                            <div className={"row"}>
                                <div className={"col-12"}>
                                    <button className={"m-4 float-end btn-style-red p-2"} onClick={() => {
                                        // @ts-ignore
                                        swal((t("inactiveOrders.warning")), {
                                            buttons: {
                                                cancel: (t("cancel")),
                                                confirm: (t("delete"))
                                            }, "dangerMode": true,
                                            "icon": "warning",
                                            "title": (t("inactiveOrders.irreversible")),
                                        }).then((willDelete: boolean) => {
                                            if (willDelete) {
                                                deletePermantlyTheOrder();
                                            }
                                        })
                                    }}>
                                        {t("delete")}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}