import "./BannerMain.css";
import {useEffect, useState} from "react";
import Cookies from "js-cookie";
import React from "react";
import {useTranslation} from "react-i18next";


// @ts-ignore
function BannerMainPage({image}) {

    const [name, setName] = useState("")
    const [t,] = useTranslation();

    useEffect(() => {

        getUsername()
    }, [])

    const getUsername = async () => {
        if (Cookies.get("username") !== undefined) {
            // @ts-ignore
            setName(Cookies.get("username"))
        } else {
            let counter = 0;
            while (Cookies.get("username") === undefined) {

                await new Promise(r => setTimeout(r, 1000));
                

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

    return (
        <div className={"banner-box"} style={{
            backgroundImage: `linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0)), url(${image})`
        }}>
            <div className={"row row-banner m-4 m-s-0"}>
            <h1 id={"greeting"} className={"mt-5 anonymous-banner-title"}>{t("banner.greetingCustomer")} {name}!</h1>

                <div className={"col-12 d-flex flex-column flex-md-row"}>
                        <button className={"banner-btn mb-4"}
                                onClick={() => {
                                    window.location.href = "/profile/orders"
                                }}>{t("banner.pastOrders")}
                        </button>
                        <button className={"banner-btn mb-4 ms-md-4"}
                                onClick={() => {
                                    window.location.href = "/profile/orders"
                                }}>{t("banner.viewCurrentOrders")}
                        </button>
                        <button className={"banner-btn mb-4 ms-md-4"}
                                onClick={() => {
                                    window.location.href = "/requestOrder"
                                }}>{t("banner.requestAnOrderNow")}
                        </button>
                    </div>

                </div>
        </div>
    )
}

export default BannerMainPage;
