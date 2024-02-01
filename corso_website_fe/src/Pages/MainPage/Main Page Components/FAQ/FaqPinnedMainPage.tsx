import {useEffect, useState} from "react";
import axios from "axios";
import {FAQ, FaqTileMainPage} from "./FaqTileMainPage";
import {useAuth} from "../../../../security/Components/AuthProvider";
import React from "react";
import {useTranslation} from "react-i18next";
import "./FaqTile.css"
import arrowImg from "../../../../ressources/images/arrow.svg"

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
        <div className="faq-container faq-mainPage-container">
            <div className="row">
                <div className="col-md-5">
                    <h1 className="faq-title-mainPage">{t("faq")}</h1>
                    <p className="faq-blurb">{t("faqBlurb")}</p>
                    <div className="row flex-row">
                        <div className="col-8">
                            <a href="/faq" className="view-more-faq">View More Faq</a>
                        </div>
                        <div className="col-4 align-items-left">
                            <img src={arrowImg} alt="arrow" className="arrow"/>
                        </div>
                    </div>
                </div>
                <div className="col-md-7">
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
                                question={value.question}
                                // @ts-ignore
                                answer={value.answer}
                            />
                        </div>
                    ))}
                </div>

            </div>
        </div>
    );
}

export default FaqPinnedMainPage