import Order from "../../../ressources/Models/Order";
import React, {useEffect, useState} from "react";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import {useParams} from "react-router-dom";
import axios from "axios";
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import './ViewSpecificOrder.css';
import {ProgressBar, Step} from "react-step-progress-bar";
import "react-step-progress-bar/styles.css";
import check from "../../../ressources/images/check_done.png";
import checkToDo from "../../../ressources/images/check_notDone.png";
import {useNavigate} from "react-router";
import Popup from "reactjs-popup";
import {SyncLoader} from "react-spinners";
import {CCarousel, CCarouselItem} from "@coreui/react";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

function ViewSpecificOrder() {

    const navigate = useNavigate();
    const {t} = useTranslation();

    const [order, setOrder] = useState<Order>(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    let {orderId} = useParams();

    const [showCancelOrderModal, setShowCancelOrderModal] = useState(false);


    const [loading, setLoading] = useState(false);

    const auth = useAuth()

    const [messagesLoading,] = useState([t("load.loadingMessage1"), t("load.loadingMessage2"), t("load.loadingMessage3")]);

    const getOrder = () => {
        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/${orderId}`,
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


    const handleCancelOrder = (event: any) => {
        event.preventDefault();
        setLoading(true);
        axios.delete(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/${orderId}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                    'Content-Type': 'application/json'
                }, data: {
                    reason: event.target.reason.value
                }
            }
        )
            .then(res => {
                if (res.status === 204) {
                    // @ts-ignore
                    swal(t("alerts.order.cancelledOrderTitle"), t("alerts.order.cancelledOrderMessage"), "success")
                        .then(() => {
                            navigate(-1);
                        })
                }
            }).catch(
            err => {

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }
            }
        ).finally(() => {
                setLoading(false);
            }
        )
    }

    useEffect(() => {
        getOrder()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const handleCancelOrderModal = () => {
        setShowCancelOrderModal(!showCancelOrderModal);
    }

    const backButtonHref = "/";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />
            <div className="container specific-order">
                <div className="row">
                    <h1 className="display-4 title text-center" id={"title"}>{t("order.order")} #{order.orderTrackingNumber}</h1>
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
                                    <button className={"m-4 float-end btn-style p-2"} onClick={() => {
                                        window.location.href = `/profile/reviews/create`;
                                    }}>{t("reviews.leaveAReview")}
                                    </button>
                                </div>
                                {order.orderStatus.toLowerCase() === "pending" &&
                                    <div className={"col-12"}>


                                        <button id={"edit-request"}
                                                className={"m-4 float-end btn-style p-2"}
                                                onClick={() => {
                                                    window.location.href = `/profile/manage/orders/${orderId}`;
                                                }
                                                }
                                        >
                                            {t("order.editRequestButton")}
                                        </button>
                                    </div>


                                }
                                {order.orderStatus.toLowerCase() === "pending" &&

                                    <Popup trigger={<div className={"col-12"}>
                                        <button className={"m-4 float-end btn-style-red p-2"}
                                                onClick={handleCancelOrderModal}
                                        >{t("order.cancelOrderButton")}
                                        </button>
                                    </div>
                                    } modal>
                                        <div className="popup-box">
                                            {loading &&
                                                <div className={"spinner-box text-center"}>
                                                    <CCarousel interval={1500}>
                                                        {messagesLoading.map((message: String) => (

                                                            <CCarouselItem key={message.length}
                                                                           style={{height: "100%"}}>
                                                                <h3 className={"text-center mb-3"}>{message}</h3>
                                                            </CCarouselItem>
                                                        ))
                                                        }

                                                    </CCarousel>

                                                    <SyncLoader className={"spinner"} color="#054AEB"/>
                                                </div>
                                            }
                                            <form onSubmit={handleCancelOrder}
                                                  className={"p-4" + (loading ? " hidden" : "")}>
                                                <div className="row">
                                                    <div className="col-12">
                                                        <h1 className={"text-center"}>{t("order.cancelRequestText")}</h1>
                                                    </div>
                                                </div>
                                                <div className="row">
                                                    <div className="col-12">
                                                        <textarea className="form-control" id="reason"
                                                                  placeholder={t("order.cancelRequestReason")} required/>
                                                    </div>
                                                    <div className="row mt-3">
                                                        <div className="col-12 text-center">
                                                            <button type="submit"
                                                                    className="btn btn-style">
                                                                {t("submitButton")}
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>


                                        </div>
                                    </Popup>
                                }


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ViewSpecificOrder;
