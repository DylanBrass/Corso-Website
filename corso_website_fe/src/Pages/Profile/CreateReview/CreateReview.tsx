import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useAuth} from "../../../security/Components/AuthProvider";
import {Rating} from "@mui/material";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import logo from "../../../ressources/images/customcolor_text-logoname_transparent_background 2.png";

import './CreateReview.css';
import Cookies from "js-cookie";
import {useTranslation} from "react-i18next";

function CreateReview() {

    const auth = useAuth();
    const [t] = useTranslation();
    const leaveReview = (event: any) => {

        event.preventDefault();

        if (event.target.review.value.length < 10) {
            // @ts-ignore
            swal(t("warningAlertTitle"), t("reviewConstraintMessage"), "warning");
            return;
        }

        if (rating === 0) {
            // @ts-ignore
            swal(t("warningAlertTitle"), t("selectRatingMessage"), "warning");
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
                    swal(t("success"), t("submitReviewSuccessMessage"), "success").then(
                        () => window.location.href = '/profile/reviews'

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
                    <h1>{t("leavePrivateReviewTitle")}</h1>
                    <h3>{t("leavePrivateReviewBlurb")}</h3>
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
                                    <h3>{t("leaveAReview")}</h3>
                                    <form onSubmit={leaveReview} className={"m-auto w-100"}>


                                        <div className="form-group">
                                            <label className={"review-label"} htmlFor="review">{t("review")}</label>
                                            <textarea className="form-control" id="review" rows={3}
                                                      placeholder={t("enterReview")} required
                                                      minLength={10}/>
                                        </div>
                                        <div className="form-group">
                                            <label className={"review-label"} htmlFor="review">{t("fullName")}</label>
                                            <input type="text" className="form-control" id="customerFullName"
                                                   placeholder={t("enterFullName")} required minLength={3}/>
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
                                        <button type="submit" className="btn btn-primary">{t("submitButton")}</button>
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
