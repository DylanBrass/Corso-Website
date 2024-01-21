import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import './ViewOnGoingOrderDetailsPageAdmin.css';
import "react-step-progress-bar/styles.css";
import Order from "../../Models/Order";
import {useAuth} from "../../security/Components/AuthProvider";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import ServiceOrder from "../../Models/ServiceOrder";
import {ProgressBar, Step} from "react-step-progress-bar";
import check from "../../images/check.svg";
import swal from "sweetalert";
import Popup from "reactjs-popup";
import {SyncLoader} from "react-spinners";
import React from "react";

function ViewSpecificOrder() {

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
            const response = await axios.patch(process.env.REACT_APP_BE_HOST+`api/v1/corso/orders/manage/cancelOrder/${orderId}`, {
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
                swal("Cancelled!", `The order with tracking number ${order.orderTrackingNumber} has been successfully cancelled.`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", "Something went wrong. Please try again.", "error");
            }

            setLoading(false)
        } catch (error) {
            
            swal("Failed!", `Failed to cancel the order with tracking number ${order.orderTrackingNumber}.`, "error");
        }
    }

    useEffect(() => {
        
    }, [loading]);


    return (
        <div>
            <NavigationBar/>
            <div className="container specific-order">
                <div className="row">
                    <h1 className="display-4 title text-center" id={"title"}>Order #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 info-box">
                        <div className="row text-start">
                            <h1>Client</h1>
                            <p>{order.customerFullName}</p>
                        </div>
                        <div className="row text-start overflow-scroll">
                            <h1>Address</h1>
                            <p >
                                Address
                                : {order.customerAddress} {order.customerApartmentNumber !== null ? "Apartment " + order.customerApartmentNumber : ""}, {order.customerCity}  </p>
                            <p>Postal code : {order.customerPostalCode}</p>
                        </div>
                        <div className="row text-start">
                            <h1>Contact Information</h1>
                            <p>Email : {order.customerEmail}</p>
                            {(order.customerPhone !== null && order.customerPhone !== "") &&
                                <p> Phone Number : {order.customerPhone}</p>}
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
                            <h1>Order Status</h1>
                            <p className={"text-capitalize"}>{
                                order.orderStatus.replace("_", " ").toLowerCase()
                            }</p>
                        </div>
                    </div>
                    <div className="col-lg-6 col-md-12">
                        <div className={"row"}>
                            <h4>Progression</h4>

                            <div className="col-lg-12 col-md-12 p-4">
                                {order.estimatedDuration > 0 && order.daysLeft >= 0 ? (
                                    <ProgressBar
                                        hasStepZero={true}
                                        percent={order.estimatedDuration / order.daysLeft* 100}
                                        filledBackground="#47B2FF"
                                        height={3}
                                    >
                                        {Array.from({ length: order.estimatedDuration + 1 }, (_, index) => (
                                            <Step transition="scale" key={index}>
                                                {({ accomplished, position }) => (
                                                    position !== 0 ? (
                                                        <div className={`w-100 progress-bar-order-step indexedStep ${accomplished ? "accomplished" : null}`}>
                                                            {accomplished ? (
                                                                <img src={check} alt={"accomplished"} />
                                                            ) : (
                                                                <span className={`step-num rounded-circle p-1`}>{index}</span>
                                                            )}
                                                        </div>
                                                    ) : (
                                                        <div className={`start-step`}></div>
                                                    )
                                                )}
                                            </Step>
                                        ))}
                                    </ProgressBar>
                                ) : (
                                    <p className="error-message">
                                        {order.estimatedDuration <= 0 ?
                                            "No estimated duration set." :
                                            "No days left count was set."}
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
                                        (order.progressInformation === null || order.progressInformation.length <0)  &&
                                        <p>No notes are currently recorded</p>

                                    }
                                </div>
                            </div>
                            <div className={"col-12"}>
                                <button className={"m-4 float-end complete-order-btn p-2"}>Complete This Order</button>
                            </div>
                            <div className={"col-12"}>
                                <button className={"m-4 float-end edit-order-btn p-2"}>Edit This Order</button>
                            </div>
                            <div className={"col-12"}>
                                <button className={"m-4 float-end cancel-order-btn p-2"} onClick={() => setOpenPopup(true)}>Cancel This Order</button>
                                    <Popup open={openPopup} closeOnDocumentClick onClose={() => setOpenPopup(false)}>
                                        <div className="popup-box">
                                            {loading &&
                                                <div className={"spinner-box text-center"}>
                                                    <h3 className={""}>
                                                        Sending email...
                                                    </h3>
                                                    <SyncLoader className={"spinner"} color="#054AEB"/>
                                                </div>
                                            }
                                            <form onSubmit={cancelStatus} className={" decline-popup" + (loading ? " hidden " : " ")}>
                                                <h4>Reason for Canceling</h4>
                                                <textarea className="view-specific-order-admin-textarea"
                                                          value={reasonForCancellation}
                                                          onChange={(e) => setReasonForCancellation(e.target.value)}
                                                          placeholder="Enter the reason for cancelling this order"
                                                />
                                                <button type={"submit"} className={"decline-popup-button"}>Submit</button>
                                            </form>
                                        </div>
                                    </Popup>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ViewSpecificOrder;
