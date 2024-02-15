import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import './ViewSpecificOrderAdmin.css';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useAuth} from "../../../security/Components/AuthProvider";
import swal from "sweetalert";
import Popup from "reactjs-popup";
import {SyncLoader} from "react-spinners";
import Order from "../../../ressources/Models/Order";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import React from "react";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

function ViewSpecificOrderAdmin() {

    const auth = useAuth()

    let {orderId} = useParams();

    const [loading, setLoading] = useState(false);

    const [reasonForCancellation, setReasonForCancellation] = useState('');
    const [openPopup, setOpenPopup] = useState(false);

    const {t} = useTranslation();


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
                if(order.orderStatus.toUpperCase() === "COMPLETED" || order.orderStatus.toUpperCase() === "CANCELLED" || order.orderStatus.toUpperCase() === "DECLINED") {
                    window.location.href = `/manage/orders/inactive/${orderId}`;
                }else if(order.orderStatus.toUpperCase() === "IN_PROGRESS") {
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

    const requestInfo = (event: any) => {

        event.preventDefault();

        const email = event.target[0].value;
        const subject = event.target[1].value;
        const message = event.target[2].value;

        setLoading(true)

        axios.post(process.env.REACT_APP_BE_HOST + 'api/v1/corso/email', {
            recipient: email,
            subject: subject,
            message: message
        }, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    swal((t("alerts.email.emailSentSuccessAlertTitle")), (t("alerts.email.emailSentMessage")), "success")
                        .then(() => {
                        })
                }
            })
            .catch(e => {
                if (e.response.data.message === "" || e.response.data.message === undefined) {
                    swal((t("alerts.error")), (t("alerts.somethingWentWrong")), "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                } else {
                    swal((t("alerts.error")), e.response.data.message, "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                }
            })
            .finally(() => {
                setLoading(false)
            })


    }

    useEffect(() => {
        
    }, [loading]);

    const acceptStatus = async () => {
        try {
            const response = await axios.patch(process.env.REACT_APP_BE_HOST+`api/v1/corso/orders/manage/acceptOrder/${orderId}`, {
                recipient: order.customerEmail
            }, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            });
            if (response.status === 200) {
                swal((t("alerts.accepted")), `${t('alerts.order.orderWithTrackingNumber')} ${order.orderTrackingNumber} ${t("alerts.order.hasBeenAccepted")}`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", (t("alerts.tryAgain")), "error");
            }
        } catch (error) {

            
            swal((t("alerts.failed")), `${t("alerts.order.failedToAcceptOrder")} ${order.orderTrackingNumber}.`, "error");
        }
    }

    const declinedStatus = async (event: any) => {

        event.preventDefault();

        setLoading(true);

        try {
            const response = await axios.patch(process.env.REACT_APP_BE_HOST+`api/v1/corso/orders/manage/declineOrder/${orderId}`, {
                reasonForDecline: event.target[0].value,
                recipient: order.customerEmail
            }, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            if (response.status === 200) {
                swal(t("alerts.declined"), `${t("alerts.order.orderWithTrackingNumber")} ${order.orderTrackingNumber} ${t("alerts.order.hasBeenDeclined")}.`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", (t("alerts.tryAgain")), "error");
            }

            setLoading(false)
        } catch (error) {
            
            swal((t("alerts.declined")), `${t("alerts.order.failedToDeclineOrder")} ${order.orderTrackingNumber}.`, "error");
        }
    }


    useEffect(() => {
        getOrder()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const [order, setOrder] = useState(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));


    const backButtonHref = "/orders/pending";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="container specific-order-admim">
                <div className="row">
                    <h1 className="display-4 title">{t("order.request")} #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 request-info-box">
                        <div className="row text-start">
                            <h1>{t("customerInfo.DueDate")}</h1>
                            <p>{order.dueDate}</p>
                        </div>
                        <div className="row text-start">
                            <h1>{t("customerInfo.customer")}</h1>
                            <p>{order.customerFullName}</p>
                        </div>
                        <div className="row text-start">
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
                                <p>{t("customerInfo.phoneNumber")}: {order.customerPhone}</p>}
                        </div>
                        <div className="row text-start">
                            <h1>Service</h1>
                            <p>{`${getValuesFromJSON(i18n.language, order.service.serviceName)}`}</p>
                        </div>
                        <div className="row text-start">
                            <h1>Description</h1>
                            <p>{order.orderDescription}</p>
                        </div>
                    </div>
                    <div className="col-lg-6 col-md-12">
                        <div className="row">
                            <button className={"btn-style-green accept-button"} onClick={acceptStatus}>{t("accept")}</button>
                        </div>
                        <div className="row">
                            <button className={"btn-style-red decline-button"} onClick={() => setOpenPopup(true)}>{t("decline")}</button>
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
                                <form onSubmit={declinedStatus} className={" decline-popup" + (loading ? " hidden " : " ")}>
                                    <h4>{t("order.declineOrderReason")}</h4>
                                    <textarea className="view-specific-order-admin-textarea"
                                        value={reasonForCancellation}
                                        onChange={(e) => setReasonForCancellation(e.target.value)}
                                        placeholder={t("order.reasonForDeclining")}
                                    />
                                    <button type={"submit"} className={"decline-popup-button"}>{t("submitButton")}</button>
                                </form>
                                </div>
                            </Popup>

                        </div>
                        <div className="row">

                            <Popup trigger={<button className={"btn-style email-button"}>{t("emailClient")}</button>} modal>
                                <div className="popup-box">
                                    {loading &&
                                        <div className={"spinner-box text-center"}>
                                            <h3 className={""}>
                                                {t("load.sendEmailLoading")}
                                            </h3>
                                            <SyncLoader className={"spinner"} color="#054AEB"/>
                                        </div>
                                    }
                                    <form onSubmit={requestInfo} className={"p-4" + (loading ? " hidden" : "")}>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="email">{t("customerInfo.email")}</label>
                                                    <input type="email" className="form-control" id="email"
                                                           placeholder={t("enterEmail")}
                                                           defaultValue={order.customerEmail}/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="subject">{t("subject")}</label>
                                                    <input type="text" className="form-control" id="subject"
                                                           placeholder={t("enterSubject")}
                                                           defaultValue={"CORSO Order-" + order.orderTrackingNumber + ": Additional Information Required"}/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="message">Message</label>
                                                    <textarea className="form-control" id="message" rows={3}
                                                              placeholder={t("enterMessage")}/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row m-5">
                                            <div className="col-12 text-center">
                                                <button type="submit" className="btn btn-primary send-email-btn">
                                                    {t("submitButton")}
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </Popup>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ViewSpecificOrderAdmin;