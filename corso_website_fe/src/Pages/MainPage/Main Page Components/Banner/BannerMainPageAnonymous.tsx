import React from "react";
import "./BannerMain.css";
import {useTranslation} from "react-i18next";


// @ts-ignore
function BannerMainPageAnonymous({image}) {

    const {t} = useTranslation();

    return (
        <div className={"banner-box"} style={{
            backgroundImage: `linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0)), url(${image})`
        }}>
            <div className={"row row-banner m-4 m-s-0"}>
                <div className={"row d-lg-flex"}>
                    <div className={" d-lg-block"}>
                        <h1 className={"mt-5 anonymous-banner-title"}>{t("banner.anonymousBannerWelcome")}</h1>
                        <p className={"anonymous-banner-subtitle"}>{t("banner.anonymousBannerRegisterPrompt")}</p>
                    </div>
                </div>

                <div className={"row"}>
                    <div className={"col-12"}>
                        <button className={"banner-btn mb-4"}
                                onClick={() => {
                                    window.location.href = process.env.REACT_APP_BE_HOST + "oauth2/authorization/okta"
                                }}>
                            {t("banner.registerNow")}
                        </button>
                    </div>



                </div>

            </div>
        </div>
    )
}

export default BannerMainPageAnonymous;