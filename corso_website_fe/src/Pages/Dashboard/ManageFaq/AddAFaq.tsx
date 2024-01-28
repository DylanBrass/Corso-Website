import React, { useState } from 'react';
import './AddAFaq.css';  // Make sure to include your original CSS file
import axios from 'axios';
import swal from 'sweetalert';
import { useNavigate } from 'react-router-dom';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import pen from "../../../ressources/images/pen.svg";
import {useAuth} from "../../../security/Components/AuthProvider";


function AddAFaq() {
    const [question, setQuestion] =  useState('');
    const [answer, setAnswer] =  useState('');
    // const [preference] =  useState('');

    //const [faqid, setFaqid] = useState<FAQ>('');
    const auth = useAuth();
    const navigate = useNavigate();

    const addAFaq = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await axios.post(
                `${process.env.REACT_APP_BE_HOST}api/v1/corso/faqs`,
                {
                    question: question,
                    answer: answer,
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
            swal('FAQ Added Successfully', '', 'success');
            setQuestion('');
            setAnswer('');
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

            swal('FAQ Not Added', '', 'error');
        }
    };

    return (
        <div>
            <NavigationBar />
            <div className="add-a-faq">
                <div className="add-a-faq-title">Add a FAQ</div>
                <form onSubmit={addAFaq}>
                    <div className="add-a-faq-form">
                        <div className="add-a-faq-form-question">
                            <div className="add-a-faq-form-question-title">Question</div>
                            <div className="grid-item input-container">
                                <img className="question-faq-pen-icon" src={pen} alt="Pen Icon" />
                                <input
                                    className="question-faq-input"
                                    type="text"
                                    id="question"
                                    name="question"
                                    placeholder="Question"
                                    required
                                    value={question}
                                    onChange={(e) => setQuestion(e.target.value)}
                                />
                            </div>
                            <div className="grid-item input-container">
                                <div className="add-a-faq-form-answer-title">Answer</div>
                                <img className="answer-faq-pen-icon" src={pen} alt="Pen Icon"
                                     style={{marginBottom:"100px"}}
                                />

                                <textarea
                                    className="answer-faq-input"
                                    id="answer"
                                    name="answer"
                                    placeholder="Answer"
                                    required
                                    value={answer}
                                    onChange={(e) => setAnswer(e.target.value)}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="submit-button-container">
                        <button type="submit" className="submit-button">
                            Add FAQ
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default AddAFaq;
