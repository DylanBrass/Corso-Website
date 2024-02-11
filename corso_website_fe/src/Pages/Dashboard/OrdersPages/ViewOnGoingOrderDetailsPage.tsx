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

function ViewSpecificOrder() {

    const {t} = useTranslation();
    const [order, setOrder] = useState<Order>(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    let {orderId} = useParams();

    const auth = useAuth()

    const [loading, setLoading] = useState(false);

    const [reasonForCancellation, setReasonForCancellation] = useState('');
    const [openPopup, setOpenPopup] = useState(false);


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

            swal((t("alerts.failed")), `${t("alerts.order.failedToCancelOrder")} ${order.orderTrackingNumber}.`, "error");
        }
    }

    useEffect(() => {

    }, [loading]);


    return (
        <div>
            <NavigationBar/>
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
                            <p>{order.service.serviceName}</p>
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
                                order.orderStatus.replace("_", " ").toLowerCase()
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
                                            {order.hoursWorked} / {order.estimatedDuration} hours
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
                                <button
                                    className={"m-4 float-end complete-order-btn p-2"}>{t("order.completeOrder")}</button>
                            </div>
                            <div className={"col-12"}>
                                <button className={"m-4 float-end edit-order-btn p-2"}>{t("order.editOrder")}</button>
                            </div>
                            <div className={"col-12"}>
                                <button className={"m-4 float-end cancel-order-btn p-2"}
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

                        </div>
                    </div>
                </div>
            </div>
            <div className="custom-row text-center">
                <button
                    className="back-btn"
                    onClick={() => {
                        window.location.href = "/orders/current";
                    }}
                >
                    {t("back")}
                </button>
            </div>
        </div>
    )
}

export default ViewSpecificOrder;
