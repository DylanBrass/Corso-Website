import Order from "../../../Models/Order";
import {useEffect, useState} from "react";
import Service from "../../../Models/Service";
import {useParams} from "react-router-dom";
import axios from "axios";
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import './ViewSpecificOrder.css';
import {ProgressBar, Step} from "react-step-progress-bar";
import "react-step-progress-bar/styles.css";
import check from "../../../images/check.svg";

function ViewSpecificOrder() {

    const [order, setOrder] = useState<Order>(new Order("", "", "", new Service("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    let {orderId} = useParams();

    const auth = useAuth()

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
                console.log(err);

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


    return (
        <div>
            <NavigationBar/>
            <div className="container specific-order">
                <div className="row">
                    <h1 className="display-4 title text-center" id={"title"}>Order #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 request-info-box">
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
                                {order.estimatedDuration > 0 &&
                                    <ProgressBar
                                        hasStepZero={true}
                                        percent={order.daysLeft / order.estimatedDuration * 100}
                                        filledBackground="#47B2FF"
                                        height={3}
                                    >
                                        {
                                            Array.from({length: order.estimatedDuration + 1}, (_, index) => (
                                                <Step transition="scale" key={index}>
                                                    {({accomplished, position}) => (
                                                        position !== 0 ?
                                                            <div
                                                                className={`w-100 progress-bar-order-step indexedStep ${accomplished ? "accomplished" : null}`}
                                                            >
                                                                {
                                                                    accomplished ?
                                                                        <img src={check} alt={"accomplished"}/>
                                                                        :
                                                                        <span
                                                                            className={`step-num rounded-circle p-1`}>{index}</span>
                                                                }
                                                            </div> :
                                                            <div
                                                                className={`start-step`}
                                                            >
                                                            </div>

                                                    )}


                                                </Step>
                                            ))
                                        }

                                    </ProgressBar>
                                }
                            </div>
                            <div className={"row"}>
                                <h4 className={"notes-title"}>Electrician Notes</h4>
                                <div className={"notes-box overflow-scroll"}>
                                    {order.progressInformation !== null && order.progressInformation.length > 0 &&
                                    <p>{order.progressInformation}</p>
                                    }
                                    {
                                        (order.progressInformation === null || order.progressInformation.length <0)  &&
                                        <p>No notes yet, but when we want to inform you about something, it will appear here !</p>

                                    }
                                </div>
                            </div>
                            <div className={"row"}>
                                <div className={"col-12"}>
                                    <button className={"m-4 float-end customer-interactions-btn p-2"}>Leave a Review</button>
                                </div>
                                <div className={"col-12"}>
                                    <button className={"m-4 float-end customer-interactions-btn p-2"}>Request Edit</button>
                                </div>
                                <div className={"col-12"}>
                                    <button className={"m-4 float-end customer-interactions-btn p-2"}>Request Cancellation</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ViewSpecificOrder;