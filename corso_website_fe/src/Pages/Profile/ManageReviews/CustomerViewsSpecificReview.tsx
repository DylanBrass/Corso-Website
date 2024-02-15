import React, { useEffect, useState } from 'react';
import './CustomerViewsSpecificReview.css';
import axios from 'axios';
import { useAuth } from '../../../security/Components/AuthProvider';
import NavigationBar from '../../../Components/NavBar/NavigationBar';
import {useParams} from "react-router-dom";
import Review from "../../../ressources/Models/Review";
import star_fill from "../../../ressources/images/Star_fill.svg";
import star_empty from "../../../ressources/images/Star.svg";
import pen from "../../../ressources/images/pen.svg";
import Trash from "../../../ressources/images/Trash_light.svg";
import {SyncLoader} from "react-spinners";
import Popup from 'reactjs-popup';
import {Rating} from "@mui/material";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";

function CustomerViewsSpecificReview() {


    const [review, setReview] = useState<Review>(new Review("", "", 0, false));
    let {reviewId} = useParams();

    const auth = useAuth();
    const [t] = useTranslation();

    const [isLoading, setIsLoading] = useState<boolean>(false);


    const [isEdit, setIsEdit] = useState<boolean>(true);

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Customer')) {
            window.location.href = '/';
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const getReview = async () => {
        console.log("review id" + reviewId);
        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/reviews/${reviewId}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                const review = res.data;
                setReview(review);

                setMessage(review.message);

                setRating(review.reviewRating);
                console.log(review);
            })
            .catch(err => {


                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

            })

    }

    useEffect(() => {
        console.log(review);
    }, [review]);

    useEffect(() => {
        getReview()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const handleDelete = (event: any) => {
        event.preventDefault();

        setIsLoading(true);
        axios.delete(process.env.REACT_APP_BE_HOST + `api/v1/corso/reviews/${reviewId}`, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(res => {
                // @ts-ignore
                swal(t("alerts.review.deleteReviewAlertTitle"), t("alerts.review.deleteReviewAlertMessage"), "success").then(
                    () =>  window.location.href = '/profile/reviews'

                )
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
            .finally(() => {
                setIsLoading(false);
            });
    }

    const [rating, setRating] = useState(0);

    const [message, setMessage] = useState("");


    const filledStars = [];
    const emptyStars = [];

    for (let i = 0; i < review.reviewRating; i++) {
        filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
    }

    for (let i = review.reviewRating; i < 5; i++) {
        emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
    }


    const saveReview = (event: any) => {

        axios.patch(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/reviews/${reviewId}`, {
                message: document.getElementById("message-edit")?.getAttribute("value"),
                reviewRating: rating

            },
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                // @ts-ignore
                swal(t("alerts.review.updateReviewAlertTitle"), t("alerts.review.updateReviewAlertMessage"), "success")
                    .then(() => {
                        getReview()
                        setIsEdit(!isEdit)
                    })

            })
            .catch(err => {
                // @ts-ignore
                auth.authError(err.response.status);

                // @ts-ignore
                swal(err.response.data.message, "", "error")
            })

    }

    const backButtonHref = "/profile/reviews";

    return (
        <>
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <h1 className="review-title-view-specific-customer">{t("reviews.review")}</h1>
            <div className="container review-tile-view-specific-container">
                <div className="row">
                    <div className="col-2 review-box-stars">
                        {!isEdit &&
                            <button className="btn btn-primary" onClick={saveReview}>
                                {t("save")}
                            </button>
                        }
                    </div>
                    <div className="col-8 review-box-stars justify-content-center">
                        <div className="review-rating">
                            <Rating
                                size="large"
                                name="rating-value"
                                value={rating}
                                disabled={isEdit}
                                onChange={(event, newValue) => {
                                    if (newValue) {
                                        setRating(newValue);
                                    }
                                }}
                            />
                        </div>
                    </div>
                    <div className="col-2 review-box-icons">
                        <div className="review-icons">
                            <img src={Trash} alt="trash" onClick={handleDelete}/>
                            {!review.pinned &&
                            <img src={pen} alt="pen" onClick={() => {
                                setIsEdit(!isEdit)
                            }}/>
                            }
                        </div>
                        {isLoading && (
                            <Popup
                                open={isLoading}
                                modal
                                closeOnDocumentClick={false}
                                contentStyle={{
                                    borderRadius: '20px',
                                    padding: '30px',
                                    background: 'rgba(255, 255, 255, 1)',
                                    boxShadow: '0px 0px 20px 0px rgba(0, 0, 0, 0.5)',
                                    maxWidth: '100%',
                                    marginTop: '300px'
                                }}
                            >
                                <div className="text-center">
                                    <div style={{ marginBottom: '15px' }}>{t("load.deletingReview")}</div>
                                    <SyncLoader className="spinner" color="#054AEB" />
                                </div>
                            </Popup>
                        )}
                    </div>
                </div>
                <div className="row">
                    {review.pinned && <p className="review-message">{t("reviews.reviewPinnedMessage")}</p>}
                    <input className="review-message" id={"message-edit"} onChange={(event) => {
                        setMessage(event.target.value)
                    }} defaultValue={message} disabled={isEdit}/>
                </div>
            </div>
        </>
    );


}

export default CustomerViewsSpecificReview;
