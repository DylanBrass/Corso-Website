import React, { useEffect, useState } from "react";
import axios from "axios";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import "./ViewAllFaqThroughDashboard.css";
import View_light from "../../../ressources/images/View_light.png";
import CrossedOutEye from "../../../ressources/images/Crossed-out_Eye.png";
import {useAuth} from "../../../security/Components/AuthProvider";
import swal from "sweetalert";
import Edit_light from "../../../ressources/images/Edit_light.svg";
import plus from "../../../ressources/images/plus-services.svg";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';



interface FAQ {
    faqid: string;
    question: string;
    answer: string;
    preference: boolean;
}

function ViewAllFaqThroughDashboard() {
    const [faqs, setFaqs] = useState<FAQ[]>([]);
    const [error, setError] = useState<string | null>(null);
    const auth = useAuth();
    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = '/';
        }
    }, [auth]);

    useEffect(() => {
        const fetchFaqs = async () => {
            try {
                const response = await axios.get(
                    process.env.REACT_APP_BE_HOST + "api/v1/corso/faqs"
                );
                setFaqs(response.data);
            } catch (error) {
                
                // @ts-ignore
                setError(error.message);
            }
        };
        fetchFaqs();
    }, []);

    const updatePreference = (faqsArray: FAQ[], faqId: string) => {
        return faqsArray.map((faq) =>
            faq.faqid === faqId ? { ...faq, preference: !faq.preference } : faq
        );
    };


    const toggleFaqPreference = (faqId: string) => {
        setFaqs((prevFaqs) => updatePreference([...prevFaqs], faqId));
    };

    const handleSubmitViewableFaqs = async () => {
        try {
            const dataToSend = faqs.map((faq) => ({
                faqId: faq.faqid,
                preference: faq.preference,
            }));

            

            await axios.patch(
                process.env.REACT_APP_BE_HOST + "api/v1/corso/faqs/viewable",
                dataToSend,
                {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    },
                }
            ).then(r => {
                if (r.status === 200) {
                    swal(t("alerts.faq.viewableFAQsUpdatedTitle"), (t("alerts.faq.viewableFAQsUpdatedMessage")), "success");
                }
            }).catch(e => {
                swal(t("alerts.error"), t("alerts.faq.chooseOnly3ViewableFAQs"), "error").then(() => {
                    // @ts-ignore
                    auth.authError(e.response.status);
                });
            });
        } catch (error) {
            
            // @ts-ignore
            setError(error.message);
        }
    };
    


    const selectedFaqsCount = faqs.filter((faq) => faq.preference).length;
    const showMoreThanThreeMessage = selectedFaqsCount > 3;
    const showLessThanThreeMessage = selectedFaqsCount < 3;

    const viewSpecificFaq = (faqId: string) => {
        window.location.href = '/faqs/' + faqId;
    }

    const goToaddFaq = () => {
        window.location.href = '/manage/faqs/create';
    }

    const backButtonHref = "/dashboard";

    return (
        <div className="faqs-dashboard-container">
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div className="container faqs-container">
                <div className="row faq-header">
                    <h1 className="faq-title">{t("faqsAdminPage.allFaqs")}</h1>
                </div>
                {error && (
                    <div className="row">
                        <div className="col-12">
                            <p className="error">{t("alerts.error")}: {error}</p>
                        </div>
                    </div>
                )}

                    <img src={plus} alt={"Add FAQ"} className={"goToFAQBtn"} onClick={goToaddFaq}/>

                <div className="submission">
                    <button onClick={handleSubmitViewableFaqs} className="btn-style">
                        {t("faqsAdminPage.saveViewableFAQs")}
                    </button>
                </div>
                <div className="row">
                    <div className="col-12">
                    {showMoreThanThreeMessage || showLessThanThreeMessage ? (
                            <p className="error">
                                {showMoreThanThreeMessage
                                    ? t("faqsAdminPage.moreThan3")
                                    : showLessThanThreeMessage
                                        ? t("faqsAdminPage.lessThan3")
                                        : ""}{" "}
                                {t("faqsAdminPage.viewableFAQMessage")}
                            </p>
                        ) : (
                            <p className="success"></p>
                        )}
                    </div>
                </div>

                <div className="row">
                    {faqs.map((faq, index) => (
                        <div className="col-md-6 col-sm-12 mb-4">
                            <div className="faq-card w-100" >
                                <div className="faq-info">
                                    <div className="faq-box-question row w-100 h-100">
                                        <div className="col-6">
                                            <p className="faq-question">
                                                {getValuesFromJSON(i18n.language, faq.question)}
                                            </p>
                                        </div>
                                        <div className={"col-2"}/>
                                        <div className={"col-4 text-end"}>
                                            <button className={"edit-specific-classBtn"} onClick={() => viewSpecificFaq(faq.faqid)}>
                                                <img src={Edit_light} alt={"edit_light"}/>
                                            </button>

                                            {faq.preference ? (
                                                <button

                                                    className="faq-pinned"
                                                    onClick={() => toggleFaqPreference(faq.faqid)}

                                                >
                                                <img
                                                    src={View_light}
                                                    className={`faq-pinned-img faq-img-${index}`}
                                                    alt={"faq-pinned"}

                                                />
                                            </button>

                                            ) : (


                                                <button
                                                    className="faq-not-pinned"
                                                    onClick={() => toggleFaqPreference(faq.faqid)}
                                                >
                                                <img
                                                    src={CrossedOutEye}

                                                    className={`faq-not-pinned-img faq-img-${index}`}
                                                    alt={"faq-not-pinned"}
                                                />

                                            </button>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                <div className="faq-content">
                                    <p className="faq-message">
                                        {getValuesFromJSON(i18n.language, faq.answer)}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default ViewAllFaqThroughDashboard;
