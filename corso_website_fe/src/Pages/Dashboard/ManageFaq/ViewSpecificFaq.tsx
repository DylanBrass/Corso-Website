import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import Trash_light from "../../../ressources/images/Trash_light.svg";
import swal from "sweetalert";
import { useAuth } from "../../../security/Components/AuthProvider";
import "./ViewSpecificFaq.css";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages, getValuesFromJSON} from "../../../Services/TranslationTools";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


function ViewFaq() {
    const auth = useAuth();
    const [faqData, setFaqData] = useState({ question: '', answer: '', preference: false });
    const { faqId } = useParams();
    const {t} = useTranslation();

    useEffect(() => {
        axios
            .get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs/${faqId}`)
            .then((response) => {
                setFaqData(response.data);
            })
            .catch((error) => {
                console.error("API Error:", error);
            });
    }, [faqId]);

    const goBack = () => {
        window.location.href = "/manage/faqs";
    }

    const deleteFaq = async () => {
        try {
            const response = await axios.delete(`${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs/${faqId}`, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });

            console.log("API Response:", response.data);
            swal((t("alerts.faq.faqDeletedTitle")), "", "success").then(() => {
                goBack();
            });
        } catch (error) {
            console.error("API Error:", error);
            swal((t("alerts.faq.faqNotDeletedTitle")), "", "error");
        }
    };

    const saveFaq = async () => {
        let englishQuestion = (document.getElementById("question_en") as HTMLInputElement)?.value
        let englishAnswer = (document.getElementById("answer_en") as HTMLInputElement)?.value

        let frenchQuestion = (document.getElementById("question_fr") as HTMLInputElement)?.value
        let frenchAnswer = (document.getElementById("answer_fr") as HTMLInputElement)?.value

        const faqData = {
            question: createJSONForLanguages(["fr","en"], [frenchQuestion, englishQuestion]),
            answer: createJSONForLanguages(["fr","en"], [frenchAnswer, englishAnswer])
        };

        try {
            await axios.put(`${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs/${faqId}`, faqData, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });
            swal((t("alerts.faq.faqUpdatedTitle")), "", "success")
                .then((value) => {
                    window.location.href = "/manage/faqs";
                });

        } catch (error) {
            console.error("API Error:", error);
            swal((t("alerts.faq.faqNotUpdatedTitle")), "", "error");
        }
    };

    const backButtonHref = "/manage/faqs";

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div>
                <div>
                    <div className="container view-faq-admin-container">
                        <div className={"faq-button-container"}>
                            <button className="btn-style-red" onClick={deleteFaq}>
                                <img src={Trash_light} alt="Trash light"/>
                            </button>
                        </div>

                        <div className={"row"}>
                            <h3>Question in English</h3>
                            <input
                                id={"question_en"}
                                type="text"
                                name="question"
                                defaultValue={getValuesFromJSON("en",faqData.question)}
                                className={"input-faq-styling"}
                            />
                            <h3>Answer in English</h3>
                            <textarea
                                id={"answer_en"}
                                name="answer"
                                defaultValue={getValuesFromJSON("en",faqData.answer)}
                                className={"input-faq-styling"}

                            />
                        </div>
                        <br />
                        <div className={"row"}>
                            <h3>Question en français</h3>
                            <input
                                id={"question_fr"}
                                type="text"
                                name="question"
                                defaultValue={getValuesFromJSON("fr",faqData.question)}
                                className={"input-faq-styling"}
                            />
                            <h3>Réponse en français</h3>
                            <textarea
                                id={"answer_fr"}
                                name="answer"
                                defaultValue={getValuesFromJSON("fr",faqData.answer)}
                                className={"input-faq-styling"}
                            />
                        </div>
                        <div className={"faq-button-container"}>
                            <button className={"mt-2 btn-style"} onClick={saveFaq}>
                                {t("save")}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default ViewFaq;
