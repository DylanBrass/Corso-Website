import React from 'react';
import './AddAFaq.css';
import axios from 'axios';
import swal from 'sweetalert';
import { useNavigate } from 'react-router-dom';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import pen from "../../../ressources/images/pen.svg";
import {useAuth} from "../../../security/Components/AuthProvider";
import {useTranslation} from "react-i18next";
import {createJSONForLanguages} from "../../../Services/TranslationTools";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';




function AddAFaq() {

    const auth = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation();

    const addAFaq = async (e: React.FormEvent) => {
        e.preventDefault();

        try {

            const englishQ = (document.getElementById("question_en") as HTMLInputElement)?.value
            const frenchQ = (document.getElementById("question_fr") as HTMLInputElement)?.value

            const englishA = (document.getElementById("answer_en") as HTMLInputElement)?.value
            const frenchA = (document.getElementById("answer_fr") as HTMLInputElement)?.value

            const response = await axios.post(
                `${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs`,
                {
                    question: createJSONForLanguages(["fr","en"], [frenchQ,englishQ]),
                    answer: createJSONForLanguages(["fr","en"], [frenchA,englishA]),
                    preference: false,
                },
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            );
            console.log('API Response:', response.data);
            swal(t("alerts.faq.faqAddedTitle"), '', 'success');

            navigate('/manage/faqs');
        } catch (error) {
            console.error('API Error:', error);
                // @ts-ignore
            if (error.response) {
                // @ts-ignore
                console.error('Response Data:', error.response.data);
                // @ts-ignore
                console.error('Response Status:', error.response.status);
                // @ts-ignore
                console.error('Response Headers:', error.response.headers);
                // @ts-ignore
            } else if (error.request) {
                // @ts-ignore
                console.error('No response received. Request details:', error.request);
            } else {
                // @ts-ignore
                console.error('Error during request setup:', error.message);
            }

            swal(t("alerts.faq.faqNotAddedTitle"), '', 'error');
        }
    };

    const backButtonHref = "/manage/faqs";

    return (
        <div>
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="add-a-faq">
                <div className="add-a-faq-title">{t("faqsAdminPage.addFAQ")}</div>
                <form onSubmit={addAFaq}>
                    <div className="add-a-faq-form">
                        <div className="add-a-faq-form-question">
                            <div className="add-a-faq-form-question-title">Question in English</div>
                            <div className="grid-item input-container">
                                <img className="question-faq-pen-icon" src={pen} alt="Pen Icon" />
                                <input
                                    className="question-faq-input"
                                    type="text"
                                    id="question_en"
                                    name="question"
                                    placeholder="Question in English"
                                    required
                                />
                            </div>
                            <div className="grid-item input-container">
                                <div className="add-a-faq-form-answer-title">Answer</div>
                                <img className="answer-faq-pen-icon" src={pen} alt="Pen Icon"
                                     style={{marginBottom:"100px"}}
                                />

                                <textarea
                                    className="answer-faq-input"
                                    id="answer_en"
                                    name="answer"
                                    placeholder="Answer"
                                    required
                                />
                            </div>
                        </div>
                        <div className="add-a-faq-form-question">
                            <div className="add-a-faq-form-question-title">Question en français</div>
                            <div className="grid-item input-container">
                                <img className="question-faq-pen-icon" src={pen} alt="Pen Icon" />
                                <input
                                    className="question-faq-input"
                                    type="text"
                                    id="question_fr"
                                    name="question"
                                    placeholder="Question en français"
                                    required
                                />
                            </div>
                            <div className="grid-item input-container">
                                <div className="add-a-faq-form-answer-title">Réponse</div>
                                <img className="answer-faq-pen-icon" src={pen} alt="Pen Icon"
                                     style={{marginBottom:"100px"}}
                                />

                                <textarea
                                    className="answer-faq-input"
                                    id="answer_fr"
                                    name="answer"
                                    placeholder="Réponse"
                                    required
                                />
                            </div>
                        </div>
                    </div>
                    <div className="submit-button-container">
                        <button type="submit" className="btn-style">
                            {t("faqsAdminPage.addFAQ")}
                        </button>
                    </div>
                </form>
            </div>
            <br />
        </div>
    );
}

export default AddAFaq;
