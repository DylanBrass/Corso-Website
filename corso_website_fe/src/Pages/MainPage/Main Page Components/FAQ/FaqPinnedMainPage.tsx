import {useEffect, useState} from "react";
import axios from "axios";
import {FAQ, FaqTileMainPage} from "./FaqTileMainPage";
import {useAuth} from "../../../../security/Components/AuthProvider";
import React from "react";
import {useTranslation} from "react-i18next";
import "./FaqTile.css"
import arrowImg from "../../../../ressources/images/arrow.svg"
import {getValuesFromJSON} from "../../../../Services/TranslationTools";
import i18n from "i18next";

function FaqPinnedMainPage() {

    const auth = useAuth()

    const {t} = useTranslation();

    const [preferredFaq, setPreferredFaq] = useState([FAQ])
    const getPreferredFaqs = () => {
        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/faqs/preferred",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                setPreferredFaq(r.data)
            })
            .catch(e => {
                
            })
    }


    useEffect(() => {
        getPreferredFaqs()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div className="faq-container faq-mainPage-container d-flex justify-content-center">
            <div style={{gap: "30px"}} className="d-flex flex-row flex-wrap justify-content-center flex-lg-nowrap">
                <div className="custom-w-small">
                    <h1 className="faq-title-mainPage">{t("faq")}</h1>
                    <p className="faq-blurb">{t("mainPage.faqBlurb")}</p>
                    <div className="d-flex flex-row gap-3" style={{cursor: "pointer"}}
                         onClick={() => {
                             window.location.href = "/faq";
                         }}
                    >
                        <div className="">
                            <a href="/faq" className="view-more-faq">{t("mainPage.viewMoreFaq")}</a>
                        </div>
                        <div className="">
                            <img src={arrowImg} alt="arrow" className="arrow"/>
                        </div>
                    </div>
                </div>
                <div className="w-50 visible-m-up">
                    {preferredFaq.map((value, index) => (
                        <div
                            // @ts-ignore
                            key={value.id}
                            style={index < 3 ? { marginTop: '1rem' } : {}}
                            className={index < 3 ? "faq-item-small-screen" : ""}
                        >
                            <FaqTileMainPage
                                // @ts-ignore
                                id={value.id}
                                // @ts-ignore
                                question={getValuesFromJSON(i18n.language, value.question)}
                                // @ts-ignore
                                answer={getValuesFromJSON(i18n.language, value.answer)}
                            />
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default FaqPinnedMainPage