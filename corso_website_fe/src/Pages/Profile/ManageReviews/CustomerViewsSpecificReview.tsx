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
import {useNavigate} from "react-router";
import {SyncLoader} from "react-spinners";
import Popup from 'reactjs-popup';

function CustomerViewsSpecificReview() {


    const [review, setReview] = useState<Review>(new Review("", "", 0, false));
    let {reviewId} = useParams();

    const auth = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState<boolean>(false);

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Customer')) {
            window.location.href = 'http://localhost:3000/';
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
                swal("Review deleted successfully!", "Your review has been successfully deleted!", "success").then(
                    () => navigate(-1)
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

    const filledStars = [];
    const emptyStars = [];

    for (let i = 0; i < review.reviewRating; i++) {
        filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
    }

    for (let i = review.reviewRating; i < 5; i++) {
        emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
    }

    return (
        <>
            <NavigationBar />
            <h1 className="review-title-view-specific-customer">Your Review</h1>
            <div className="container review-tile-view-specific-container">
                <div className="row">
                    <div className="col-2 review-box-stars">
                        {/* Empty column for spacing */}
                    </div>
                    <div className="col-8 review-box-stars justify-content-center">
                        <div className="review-rating">
                            {filledStars}
                            {emptyStars}
                        </div>
                    </div>
                    <div className="col-2 review-box-icons">
                        <div className="review-icons">
                            <img src={Trash} alt="trash" onClick={handleDelete}/>
                            <img src={pen} alt="pen" onClick={() => {
                                //to implement
                            }}/>
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
                                    <div style={{ marginBottom: '15px' }}>Deleting Review ...</div>
                                    <SyncLoader className="spinner" color="#054AEB" />
                                </div>
                            </Popup>
                        )}
                    </div>
                </div>
                <div className="row">
                    {review.pinned && <p className="review-message">Your review is pinned to the landing page</p>}
                    <p className="review-message">{review.message}</p>
                </div>
            </div>
        </>
    );


}

export default CustomerViewsSpecificReview;
