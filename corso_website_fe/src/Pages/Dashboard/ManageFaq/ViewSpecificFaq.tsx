import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import Trash_light from "../../../ressources/images/Trash_light.svg";
import swal from "sweetalert";
import { useAuth } from "../../../security/Components/AuthProvider";
import "./ViewSpecificFaq.css";

function ViewFaq() {
    const auth = useAuth();
    const [faqData, setFaqData] = useState({ question: '', answer: '', preference: false });
    const { faqId } = useParams();

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
            swal("FAQ Deleted Successfully", "", "success").then(() => {
                goBack();
            });
        } catch (error) {
            console.error("API Error:", error);
            swal("FAQ Not Deleted", "", "error");
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFaqData({ ...faqData, [e.target.name]: e.target.value });
    };

    const saveFaq = async () => {
        try {
            await axios.put(`${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs/${faqId}`, faqData, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });
            swal("FAQ Updated Successfully", "", "success")
                .then((value) => {
                    window.location.href = "/manage/faqs";
                });

        } catch (error) {
            console.error("API Error:", error);
            swal("FAQ Not Updated", "", "error");
        }
    };

    return (
        <div>
            <NavigationBar/>
            <div>
                <div className="faq-button-container">
                    <button className="specific-faq-back-btn" onClick={goBack}>Back</button>
                </div>
                <div>
                    <div className="container view-faq-admin-container">
                        <div className={"faq-button-container"}>
                            <button className="specific-trash-faq-img" onClick={deleteFaq}>
                                <img src={Trash_light} alt="Trash light"/>
                            </button>
                        </div>

                        <div className={"row"}>
                            <h3>Question</h3>
                            <input
                                type="text"
                                name="question"
                                value={faqData.question}
                                onChange={handleInputChange}
                                className={"input-faq-styling"}
                            />
                        </div>
                        <div className={"row"}>
                            <h3>Answer</h3>
                            <textarea
                                name="answer"
                                value={faqData.answer}
                                onChange={handleInputChange}
                                className={"input-faq-styling"}

                            />
                        </div>
                        <div className={"faq-button-container"}>
                            <button className={"save-faq-button"} onClick={saveFaq}>
                                Save
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default ViewFaq;
