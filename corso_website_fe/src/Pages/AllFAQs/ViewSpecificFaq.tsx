import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./ViewSpecificFaq.css";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import FAQ from "../../Models/FAQ";
import Trash_light from "../../images/Trash_light.svg";
import swal from "sweetalert";
import { useAuth } from "../../security/Components/AuthProvider";

function ViewFaq() {
    const auth = useAuth();
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
            swal("FAQ Deleted Successfully", "", "success");
            goBack();
        } catch (error) {
            console.error("API Error:", error);
            swal("FAQ Not Deleted", "", "error");
        }
    };





    return (
        <div>
            <NavigationBar/>

            <div className="specific-view-faq-container">
                    <button className={"specific-faq-back-btn"} onClick={goBack}>Back</button>
                <div className="specific-faq-container">
                    {specificFaq && (
                        <div className="specific-faq-main-box">

                            <button className="specific-trash-faq-img" onClick={deleteFaq}>
                                <img src={Trash_light} alt="Trash light"/>
                            </button>

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
