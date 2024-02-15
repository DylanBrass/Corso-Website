import NavigationBar from "../../Components/NavBar/NavigationBar";
import React, {useEffect, useState} from "react";
import Cookies from "js-cookie";
import {useAuth} from "../../security/Components/AuthProvider";
import "./Profile.css"

import arrow from "../../ressources/images/Expand_right_light.png"
import edit from "../../ressources/images/pen.svg"
import review from "../../ressources/images/Group 36519.svg"
import ordersCart from "../../ressources/images/Basket_alt_3_light.png"
import {Link} from "react-router-dom";
import axios from "axios";
import CurrentOrdersProgress from "./Components/CurrentOrdersProgress";
import Order from "../../ressources/Models/Order";
import {CCarousel, CCarouselItem} from "@coreui/react";
import swal from "sweetalert";
import {useTranslation} from "react-i18next";
import '../globalStyling/globalStyling.css';
import BackButton from "../../Components/BackButton";


function ProfilePage() {

    const auth = useAuth()
    const [t,] = useTranslation("translation");

    const [phone, setPhone] = useState(() => t("profilePage.noneGiven"));
    const [email, setEmail] = useState(Cookies.get("email"))
    const [name, setName] = useState("")

    const [verified, setVerified] = useState(true)


    const [orders, setOrders] = useState<Order[]>([]);



    useEffect(() => {
        fetchOrders()

        getUsername()
    }, [])


    const sendVerificationEmail = async () => {
        axios.post(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers/request/verification", {}, {
            headers: {
                // @ts-ignore
                "X-XSRF-TOKEN": auth.getXsrfToken(),
            }
        }).then(r => {
            if (r.status === 200) {
                swal(t("alerts.email.emailSentSuccessAlertTitle"), t("alerts.email.emailSentSuccessAlertMessage"), "success")
                    .then(r => {
                        document.getElementById("send-verification-btn")!.setAttribute("disabled", "true")
                    })
            }
        }).catch(e => {
            if (e.response.status === 401 || e.response.status === 403) {
                // @ts-ignore
                auth.authError(e.response.status);
            } else {
                if (e.response.status === 422) {
                    swal(t("alerts.warningAlertTitle"), (t("alerts.email.emailAlreadySent")), "warning")
                    return
                }
                swal(t("alerts.error"), e.response.data.message, "error")
            }
        })

    }

    const fetchOrders = async () => {
        try {

            await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/orders/current')
                .then(res => {
                    const orders = res.data;
                    setOrders(orders);
                })

        } catch (error) {

        }
    }


    const getUsername = async () => {
        if (Cookies.get("username") !== undefined) {
            // @ts-ignore
            setName(Cookies.get("username"))
        } else {
            let counter = 0;
            while (Cookies.get("username") === undefined) {

                await new Promise(r => setTimeout(r, 50));


                if (Cookies.get("username") !== undefined) {
                    // @ts-ignore
                    setName(Cookies.get("username"))
                    break;
                }
                counter++;
                if (counter === 100) {
                    break;
                }
            }
        }
    }


    const checkIfProfileExists = async () => {
        await axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers?simpleCheck=true", {
            headers: {
                // @ts-ignore
                "X-XSRF-TOKEN": auth.getXsrfToken(),
            }
        }).then(r => {

            if (r.status === 200) {

                axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                }).then(r => {
                    if (r.status === 200) {

                        if (r.data.phone.length > 0) {
                            setPhone(r.data.phone)
                        }

                        if (r.data.email.length > 0) {
                            setEmail(r.data.email)
                        }

                        if(r.data.verified){
                            Cookies.set("email", r.data.email);
                        }else {
                            // @ts-ignore
                            auth.getUserInfo()
                        }

                        setVerified(r.data.verified)
                    }
                }).catch(e => {

                        // @ts-ignore
                        auth.authError(e.response.status)

                    }
                )
            } else {


            }
        }).catch(e => {

// @ts-ignore
                auth.authError(e.response.status)
            }
        )


    }


    useEffect(() => {
        checkIfProfileExists()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const backButtonHref = "/";


    return (
        <div className={"h-100"} style={{
            backgroundColor: "#F3F3F3",
            minHeight: "100vh",
            overflow: "hidden",
        }}>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />

            <div className="profile-page h-100">
                <div className="row p-0 p-md-5 box-row">
                    <div className={"user-box col-12 col-lg-6 p-5"}>
                        {
                            Cookies.get("picture") !== null && Cookies.get("picture") !== undefined &&
                            // @ts-ignore
                            auth.userRoles().includes("Customer") &&
                            <img src={Cookies.get("picture")}
                                 alt="Profile" id={"profile-image-page"}/>

                        }

                        <h1 className={"mt-5"}>{name}</h1>
                        <form method={"post"} action={process.env.REACT_APP_BE_HOST + "api/v1/corso/logout"}>
                            <button id={"logout-profile"} type={"submit"} onClick={
                                () => {
                                    // @ts-ignore
                                    auth.logout()
                                }
                            }>{t("navigation.logout").toUpperCase()}
                            </button>
                        </form>
                    </div>
                    <div className={"col-12 col-lg-6 d-block mt-4 mt-lg-0"}>
                        <div className={"w-100 d-block px-lg-5"}>
                            <div className={"col-12 col-lg-6 contact-box w-100 p-2"}>
                                <h1 className={"contact-title"}>{t("customerInfo.contactInformation")}</h1>
                                <p className={"profile-text"}>{t("customerInfo.phoneNumber")}: {phone}</p>
                                <p className={"profile-text"}>{t("customerInfo.email")}: {email}
                                    <span className={"mx-5"}>
                                        {verified ?
                                            <span style={{color: "green", fontSize: "2.125rem"}}>✓</span>
                                            :
                                            <span style={{color: "red", fontSize: "2.125rem"}}>
                                                        &#x21;
                                                <button id={"send-verification-btn"}
                                                        className={"btn btn-style-yellow"}
                                                        onClick={sendVerificationEmail}>
                                                    {t("email.resendEmail")}
                                                </button>
                                                    </span>

                                        }
                                    </span>

                                </p>
                            </div>
                        </div>
                        {
                            orders.length > 0 &&
                            <div className={"w-100 d-block px-lg-5"}>
                                <div className={"current-orders-box pt-3 pb-3 overflow-scroll"}>
                                    {orders.length > 1 ?
                                        <CCarousel controls interval={5000}>
                                            {orders.map((order: Order) => (

                                                <CCarouselItem key={order.orderTrackingNumber}
                                                               style={{height: "100%"}}>
                                                    <CurrentOrdersProgress
                                                        // @ts-ignore
                                                        order={order}
                                                        key={order.orderTrackingNumber}/>

                                                </CCarouselItem>
                                            ))
                                            }

                                        </CCarousel>
                                        :
                                        <CCarousel interval={false}>
                                            {orders.map((order: Order) => (

                                                <CCarouselItem key={order.orderTrackingNumber}
                                                               style={{height: "100%"}}>
                                                    <CurrentOrdersProgress
                                                        // @ts-ignore
                                                        order={order}
                                                        key={order.orderTrackingNumber}/>

                                                </CCarouselItem>
                                            ))
                                            }

                                        </CCarousel>
                                    }

                                </div>
                            </div>
                        }

                    </div>
                </div>

                <div className={"row box-row p-0 p-md-5 pt-5 p-md-0"}>
                    <div className={"options-box col-12 col-md-6 mb-5 mb-md-0"}>
                        <div className={"row w-100 option-box"}>
                            <div className={"col-2 d-none d-xl-block"}>
                                <img src={edit} alt={"edit"} className={"m-1 float-start"}/>
                            </div>
                            <div className={"col-12 col-xl-8 option-text"}>
                                <Link className={"redirect-option"} to={"/profile/edit"}>
                                    <h3 className={"option edit-profile-option m-auto text-center text-xl-start"}>{t("profilePage.editProfile")}</h3>
                                </Link>
                            </div>
                            <div className={"col-2 text-end d-none d-xl-block m-auto"}>
                                <img src={arrow} alt={"arrow"} className={"d-none d-lg-block float-end"}/>
                            </div>
                        </div>
                        <div className={"row w-100 option-box"}>
                            <div className={"col-2 d-none d-xl-block p-3"}>
                                <img src={ordersCart} alt={"edit"} className={" mx-2 float-start"}/>
                            </div>
                            <div className={"col-12 col-xl-8 option-text"}>
                                <Link className={"redirect-option"} to={"/profile/orders"}>
                                    <h3 className={"option past-orders-option m-auto text-center text-xl-start"}>{t("profilePage.viewYourOrders")}</h3>
                                </Link>
                            </div>
                            <div className={"col-2 text-end d-none d-xl-block m-auto"}>
                                <img src={arrow} alt={"arrow"} className={"d-none d-lg-block float-end"}/>
                            </div>
                        </div>
                        <div className={"row w-100 option-box"}>
                            <div className={"col-2 d-none d-xl-block p-3"}>
                                <img src={review} alt={"edit"} className={"float-start mx-3"}/>
                            </div>
                            <div className={"col-12 col-xl-8 option-text"}>
                                <Link className={"redirect-option"} to={"/profile/reviews"}>
                                    <h3 className={"past-reviews-option option text-center text-xl-start m-auto"}>
                                        {t("profilePage.viewYourReviews")}
                                    </h3>
                                </Link>
                            </div>
                            <div className={"col-2 text-end d-none d-xl-block m-auto"}>
                                <img src={arrow} alt={"arrow"} className={"d-none d-lg-block float-end"}/>
                            </div>
                        </div>
                    </div>
                    <div className={"col-12 col-md-6"}>
                        <div className={"row w-100 px-0 px-md-5 request-order-box-profile"}>
                                <p>{t("profilePage.gotAProblemNeedingFixing")}</p>
                                <p>{t("profilePage.getStartedWithYourNextOrderHere")}</p>
                                <button
                                    className="btn btn-style"
                                    onClick={() => {
                                        window.location.href = "/requestOrder";
                                    }}
                                >
                                    {t("banner.requestAnOrderNow")}!
                                </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default ProfilePage;
