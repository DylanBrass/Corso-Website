import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./ViewSpecificFaq.css";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import FAQ from "../../Models/FAQ";
import ViewAllFaqThroughDashboard from "./ViewAllFaqThroughDashboard";

function ViewFaq() {
    const [specificFaq, setSpecificFaq] = useState<FAQ | null>(null);
    const {faqId} = useParams();

    useEffect(() => {
        console.log("FAQ ID:", faqId);

        axios
            .get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs/${faqId}`)
            .then((response) => {
                console.log("API Response:", response.data);
                setSpecificFaq(response.data);
            })
            .catch((error) => {
                console.error("API Error:", error);
            });
    }, [faqId]);

    const goBack = () => {
        window.location.href = "http://localhost:3000/manage/faqs";
    }

    return (
        <div>
            <NavigationBar/>

            <div className="specific-view-faq-container">

                    <button className={"specific-faq-back-btn"} onClick={goBack}>Back</button>

                <div className="specific-faq-container">
                    {specificFaq && (
                        <div className="specific-faq-main-box">
                            <div className="specific-faq-content">
                                <h2 className="specific-h2">{specificFaq.question}</h2>
                                <p className="specific-p">{specificFaq.answer}</p>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default ViewFaq;
