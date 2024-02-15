import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useEffect, useState} from "react";
import axios from "axios";
import {FAQ, FaqTileFaqPage} from "../../../Components/FaqTile/FaqTileFaqPage";
import {useAuth} from "../../../security/Components/AuthProvider";
import "./ViewAllFaq.css"
import React from "react";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";

function ViewAllFaq() {
    const auth = useAuth()

    const [faqs, setFaqs] = useState([FAQ])
    const [t] = useTranslation()

    const getFaqs = () => {
        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/faqs",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                setFaqs(r.data)
            })
            .catch(e => {
                
            })
    }

    useEffect(() => {
        getFaqs()

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const backButtonHref = "/"

    return (

        <div className={"remove-gutters"}>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />
            <div className={"text-center"}>
                <div className={"row remove-gutters"}>
                    <span className={"title"}>Faq</span>
                </div>
                <div className={"tiles-container remove-gutters"}>
                    {faqs.map((value) => (
                        <FaqTileFaqPage
                            //@ts-ignore
                            key={value.id}
                            //@ts-ignore
                            id={value.id}
                            // @ts-ignore
                            question={getValuesFromJSON(i18n.language, value.question)}
                            // @ts-ignore
                            answer={getValuesFromJSON(i18n.language, value.answer)}
                        />
                    ))}
                </div>
            </div>
        </div>
    )

}

export default ViewAllFaq