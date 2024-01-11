import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import './ViewSpecificOrderAdmin.css';
import NavigationBar from "../../Components/NavBar/NavigationBar";
import {useAuth} from "../../security/Components/AuthProvider";
import swal from "sweetalert";
import Popup from "reactjs-popup";
import {SyncLoader} from "react-spinners";
import Order from "../../Models/Order";
import Service from "../../Models/Service";

function ViewSpecificOrderAdmin() {

    const auth = useAuth()

    let {orderId} = useParams();

    const [loading, setLoading] = useState(false);

    const [reasonForCancellation, setReasonForCancellation] = useState('');
    const [openPopup, setOpenPopup] = useState(false);


    const getOrder = async () => {
        axios.get(`http://localhost:8080/api/v1/corso/orders/manage/${orderId}`,
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

    const requestInfo = (event: any) => {

        event.preventDefault();

        const email = event.target[0].value;
        const subject = event.target[1].value;
        const message = event.target[2].value;

        setLoading(true)

        axios.post(`http://localhost:8080/api/v1/corso/email`, {
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
                    swal("Email sent!", "Email has been sent successfully!", "success")
                        .then(() => {
                        })
                }
            })
            .catch(e => {
                if (e.response.data.message === "" || e.response.data.message === undefined) {
                    swal("Error!", "Something went wrong!", "error")
                        .then(() => {
                            // @ts-ignore
                            auth.authError(e.response.status)
                        })
                } else {
                    swal("Error!", e.response.data.message, "error")
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
        console.log(loading);
    }, [loading]);

    const acceptStatus = async () => {
        try {
            const response = await axios.patch(`http://localhost:8080/api/v1/corso/orders/manage/acceptOrder/${orderId}`, {
                recipient: order.customerEmail
            }, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            });
            if (response.status === 200) {
                swal("Accepted!", `The order with tracking number ${order.orderTrackingNumber} has been successfully accepted.`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", "Something went wrong. Please try again.", "error");
            }
        } catch (error) {
            console.error("Error updating order status: ", error);
            swal("Failed!", `Failed to accept the order with tracking number ${order.orderTrackingNumber}.`, "error");
        }
    }

    const declinedStatus = async (event: any) => {

        event.preventDefault();

        setLoading(true);

        try {
            const response = await axios.patch(`http://localhost:8080/api/v1/corso/orders/manage/declineOrder/${orderId}`, {
                reasonForDecline: event.target[0].value,
                recipient: order.customerEmail
            }, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            if (response.status === 200) {
                swal("Declined!", `The order with tracking number ${order.orderTrackingNumber} has been successfully declined.`, "success")
                    .then(() => {
                        window.location.href = '/dashboard';
                    });
            } else {
                swal("Oops!", "Something went wrong. Please try again.", "error");
            }

            setLoading(false)
        } catch (error) {
            console.error("Error updating order status: ", error);
            swal("Failed!", `Failed to decline the order with tracking number ${order.orderTrackingNumber}.`, "error");
        }
    }


    useEffect(() => {
        getOrder().then(r => console.log("Order fetched"));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const [order, setOrder] = useState(new Order("", "", "", new Service("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 0, 0));
    return (
        <div>
            <NavigationBar/>
            <div className="container specific-order-admim">
                <div className="row">
                    <h1 className="display-4 title">Request #{order.orderTrackingNumber}</h1>
                </div>
                <div className="row">
                    <div className="col-lg-6 col-md-12 request-info-box">
                        <div className="row text-start">
                            <h1>Client</h1>
                            <p>{order.customerFullName}</p>
                        </div>
                        <div className="row text-start">
                            <h1>Address</h1>
                            <p>
                                Address
                                : {order.customerAddress} {order.customerApartmentNumber !== null ? "Apartment " + order.customerApartmentNumber : ""}, {order.customerCity}  </p>
                            <p>Postal code : {order.customerPostalCode}</p>
                        </div>
                        <div className="row text-start">
                            <h1>Client Contact Information</h1>
                            <p>Email : {order.customerEmail}</p>
                            {(order.customerPhone !== null && order.customerPhone !== "") &&
                                <p> Phone Number : {order.customerPhone}</p>}
                        </div>
                        <div className="row text-start">
                            <h1>Service</h1>
                            <p>{order.service.serviceName}</p>
                        </div>
                        <div className="row text-start">
                            <h1>Description</h1>
                            <p>{order.orderDescription}</p>
                        </div>
                    </div>
                    <div className="col-lg-6 col-md-12">
                        <div className="row">
                            <button className={"accept-button"} onClick={acceptStatus}>Accept</button>
                        </div>
                        <div className="row">
                            <button className={"decline-button"} onClick={() => setOpenPopup(true)}>Decline</button>
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
                                <form onSubmit={declinedStatus} className={" decline-popup" + (loading ? " hidden " : " ")}>
                                    <h4>Reason for Declining</h4>
                                    <textarea
                                        value={reasonForCancellation}
                                        onChange={(e) => setReasonForCancellation(e.target.value)}
                                        placeholder="Enter the reason for declining the order"
                                    />
                                    <button type={"submit"} className={"decline-popup-button"}>Submit</button>
                                </form>
                                </div>
                            </Popup>

                        </div>
                        <div className="row">

                            <Popup trigger={<button className={"email-button"}>Email
                                Client</button>} modal>
                                <div className="popup-box">
                                    {loading &&
                                        <div className={"spinner-box text-center"}>
                                            <h3 className={""}>
                                                Sending email...
                                            </h3>
                                            <SyncLoader className={"spinner"} color="#054AEB"/>
                                        </div>
                                    }
                                    <form onSubmit={requestInfo} className={"p-4" + (loading ? " hidden" : "")}>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="email">Email address</label>
                                                    <input type="email" className="form-control" id="email"
                                                           placeholder="Enter email"
                                                           defaultValue={order.customerEmail}/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="subject">Subject</label>
                                                    <input type="text" className="form-control" id="subject"
                                                           placeholder="Enter subject"
                                                           defaultValue={"CORSO Order-" + order.orderTrackingNumber + ": Additional Information Required"}/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row">
                                            <div className="col-12">
                                                <div className="form-group">
                                                    <label htmlFor="message">Message</label>
                                                    <textarea className="form-control" id="message" rows={3}
                                                              placeholder="Enter message"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="row m-5">
                                            <div className="col-12 text-center">
                                                <button type="submit" className="btn btn-primary send-email-btn">
                                                    Submit
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