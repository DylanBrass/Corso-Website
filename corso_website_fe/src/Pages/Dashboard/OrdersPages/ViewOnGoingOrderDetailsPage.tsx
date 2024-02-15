import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import './ViewOnGoingOrderDetailsPageAdmin.css';
import "react-step-progress-bar/styles.css";
import Order from "../../../ressources/Models/Order";
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import {ProgressBar, Step} from "react-step-progress-bar";
import check from "../../../ressources/images/check_done.png";
import swal from "sweetalert";
import Popup from "reactjs-popup";
import {SyncLoader} from "react-spinners";
import checkToDo from "../../../ressources/images/check_notDone.png";
import {useTranslation} from "react-i18next";
import {toast} from "react-toastify";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


function ViewSpecificOrder() {

    const {t} = useTranslation();
    const [order, setOrder] = useState<Order>(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    let {orderId} = useParams();
    const [orderCompleted, setOrderCompleted] = useState(false);
    const auth = useAuth()

    const [loading, setLoading] = useState(false);

    const [reasonForCancellation, setReasonForCancellation] = useState('');
    const [openPopup, setOpenPopup] = useState(false);
    const [orderStatus, setOrderStatus] = React.useState<string>(order.orderStatus);


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
                if(order.orderStatus.toUpperCase() === "COMPLETED" || order.orderStatus.toUpperCase() === "CANCELLED" || order.orderStatus.toUpperCase() === "DECLINED") {
                    window.location.href = `/manage/orders/inactive/${orderId}`;
                }else if (order.orderStatus.toUpperCase() === "PENDING") {
                    window.location.href = `/manage/orders/${orderId}`;
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

    useEffect(() => {
        getOrder()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const cancelStatus = async (event: any) => {

        event.preventDefault();

        setLoading(true);

        try {
            const response = await axios.patch(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/manage/cancelOrder/${orderId}`, {
                reasonForDecline: event.target[0].value,
                //recipient: order.customerEmail
                recipient: "karinaevang@hotmail.com"
            }, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            if (response.status === 200) {
                swal((t("alerts.order.cancelledOrderTitle")), `${t("alerts.order.orderWithTrackingNumber")} ${order.orderTrackingNumber} ${t("alerts.order.hasBeenCancelled")}.`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", (t("alerts.tryAgain")), "error");
            }

            setLoading(false)
        } catch (error) {

            await swal((t("alerts.failed")), `${t("alerts.order.failedToCancelOrder")} ${order.orderTrackingNumber}.`, "error");
        }
    }

    useEffect(() => {

    }, [loading]);


    const goToEditOrder = () => {
        window.location.href = `/manage/updateProgression/${orderId}`
    }

    const loadingCompleteOrder = () => {
        toast((t("order.orderFinalCompleted")), {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: false,
            pauseOnHover: true,
            draggable: false,
            bodyClassName: "toast-body",
        });
    };


    const completeOrder = async () => {
        if (!orderCompleted && !loading) {
            swal({
                title: (t("order.completeMessageQuestion")),
                text: (t("order.completeMessageQuestion2")),
                icon: "warning",
                buttons: [(t("order.completeQuestionButtonsCancel")), (t("order.completeQuestionButtonsConfirm"))],
                dangerMode: true,
            }).then(async (willComplete) => {
                if (willComplete) {
                    setLoading(true);
                    let data = {
                        orderId: orderId,
                        orderStatus: "COMPLETED",
                    };

                    try {
                        loadingCompleteOrder();
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
                            swal({
                                title: `${t("order.orderCompletedMessage")} ${order.orderTrackingNumber} ${t("order.orderCompletedMessage2")}`,
                                icon: "success"
                            }).then(() => {
                                setOrderCompleted(true);
                                goBack();
                            });
                        } else {
                            swal((t("order.orderCompletedErrorMessage")), "error");
                        }
                    } catch (error) {
                        swal(`${t("order.orderCompletedErrorMessage2")} ${order.orderTrackingNumber}.`, `${t("order.orderNoProgression")}`, "error");
                    } finally {
                        setLoading(false);
                    }
                }
            });
        }
    };



    const goBack = () => {
        window.location.href = '/orders/current';
    }

    useEffect(() => {
        if(i18n.language === "fr") {
            if(order.orderStatus.toLowerCase() === "in_progress"){
                setOrderStatus("en cours");
            }
        }
        else {
            setOrderStatus(order.orderStatus)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [i18n.language, orderStatus]);

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
                    <h3 className={new Date(order.dueDate) < new Date() ? "error text-center" : "text-center"}>
                        {t("orderDate")} : {order.orderDate}
                    </h3>
                    <h3 className={new Date(order.dueDate) < new Date() ? "error text-center" : "text-center"}>
                        {t("dueDate")} : {order.dueDate}
                    </h3>

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

                            <div className="col-lg-12 col-md-12 p-4 progress-current-wrapper">
                                {order.estimatedDuration > 0 && order.hoursWorked >= 0 ? (
                                    <div>
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
                                        <h4 className={"text-center mt-3"}>
                                            {order.hoursWorked} / {order.estimatedDuration}
                                            {" "}
                                            {t("order.hours")}
                                        </h4>
                                    </div>
                                ) : (
                                    <p className="error-message">
                                        {order.estimatedDuration <= 0 ?
                                            (t("order.noEstimatedDuration")) :
                                            (t("order.noDaysLeft"))}
                                    </p>
                                )}
                            </div>

                            <div className={"row"}>
                                <h4 className={"notes-title"}>Notes</h4>
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



                            <div className={"col-12"}>

                                {!loading && (
                                    <button className={"m-4 float-end btn-style-green p-2"} onClick={completeOrder}
                                            id="completeOrderBtn"
                                            disabled={orderCompleted}>{t("order.completeOrder")}</button>
                                )}
                                {loading &&
                                    <SyncLoader color="#36d7b7" className={"m-4 float-end p-2"}/>
                                }

                            </div>

                            <div className={"col-12"}>
                                <button className={"m-4 float-end btn-style-red p-2"}
                                        onClick={() => setOpenPopup(true)}>{t("order.cancelOrderButton")}
                                </button>
                                <Popup open={openPopup} closeOnDocumentClick onClose={() => setOpenPopup(false)}>
                                    <div className="popup-box">
                                        {loading &&
                                            <div className={"spinner-box text-center"}>
                                                <h3 className={""}>
                                                    {t("load.sendEmailLoading")}
                                                </h3>
                                                <SyncLoader className={"spinner"} color="#054AEB"/>
                                            </div>
                                        }
                                        <form onSubmit={cancelStatus}
                                              className={" decline-popup" + (loading ? " hidden " : " ")}>
                                            <h4>{t("order.cancelRequestReason")}</h4>
                                            <textarea className="view-specific-order-admin-textarea"
                                                      value={reasonForCancellation}
                                                      onChange={(e) => setReasonForCancellation(e.target.value)}
                                                      placeholder={t("order.reasonForCancellation")}
                                            />
                                            <button type={"submit"}
                                                    className={"decline-popup-button"}>{t("submitButton")}</button>
                                        </form>
                                    </div>
                                </Popup>

                            </div>

                            <div className={"col-12"}>
                                <button className={"m-4 float-end btn-style p-2"} onClick={goToEditOrder}>{t("order.editOrder")}</button>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    )


}

export default ViewSpecificOrder;