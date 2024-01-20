import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router";
import {useAuth} from "../../security/Components/AuthProvider";
import {Rating} from "@mui/material";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import logo from "../../images/customcolor_text-logoname_transparent_background 2.png";

import './CreateReview.css';
import Cookies from "js-cookie";

function CreateReview() {

    const auth = useAuth();
    const navigate = useNavigate();
    const leaveReview = (event: any) => {

        event.preventDefault();

        if (rating === 0) {
            // @ts-ignore
            swal("Warning!", "Please select a rating!", "warning");
            return;
        }

        axios.post(process.env.REACT_APP_BE_HOST + `api/v1/corso/reviews`, {
            customerFullName: event.target.customerFullName.value,
            reviewRating: rating,
            message: event.target.review.value
        }, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(res => {
                if (res.status === 201) {
                    // @ts-ignore
                    swal("Success!", "Your review has been submitted!", "success").then(
                        () => navigate(-1)
                    )
                }
            })
            .catch(err => {
                

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

                if (err.response.status >= 400 && err.response.status < 500) {
                    // @ts-ignore
                    swal(err.response.data.message, "", "error");
                }

            })
    }

    const [rating, setRating] = useState(0);

    useEffect(() => {
    document.getElementById("customerFullName")!.setAttribute("value", Cookies.get("username") as string || "");

    }, []);

    return (
        <div className={"create-review-page"}>
            <NavigationBar/>
            <div className={"create-review-page-content"}>
                <div className={"create-review-page-header"}>
                    <h1>Leave a private review to Corso</h1>
                    <h3>This review will be visible by you and Corso, but we retain the right to display your review on
                        the
                        main
                        page, you will be informed when your review is added or removed from the main page.</h3>
                </div>
                <div className={"add-review-box"}>
                    <div className={"row create-box"}>
                        <div className={"col-0 col-md-6 image-box"}></div>
                        <div className={"col-12 col-md-6 review-form"}>
                            <div className={"row text-center"}>
                                <img src={logo} alt={"Corso Inc"} style={{
                                    width: "100%",
                                }}/>

                            </div>
                            <div className={"row form-box w-100"}>
                                <div className={"col-12 m-auto"}>
                                    <h3>Leave a review</h3>
                                    <form onSubmit={leaveReview} className={"m-auto w-100"}>


                                        <div className="form-group">
                                            <label className={"review-label"} htmlFor="review">Review</label>
                                            <textarea className="form-control" id="review" rows={3}
                                                      placeholder="Enter review"
                                                      minLength={10}/>
                                        </div>
                                        <div className="form-group">
                                            <label className={"review-label"} htmlFor="review">Full Name</label>
                                            <input type="text" className="form-control" id="customerFullName"
                                                   placeholder="Enter your full name" required minLength={3}/>
                                        </div>

                                        <div className="form-group">
                                            <div className="rating">
                                                <Rating
                                                    size="large"
                                                    name="rating-value"
                                                    value={rating}
                                                    onChange={(event, newValue) => {
                                                        if (newValue) {
                                                            setRating(newValue);
                                                        }
                                                    }}
                                                />
                                            </div>
                                        </div>
                                        <button type="submit" className="btn btn-primary">Submit</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CreateReview;