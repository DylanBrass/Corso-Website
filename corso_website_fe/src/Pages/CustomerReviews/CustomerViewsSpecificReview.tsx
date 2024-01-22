import React, { useEffect, useState } from 'react';
import './CustomerViewsSpecificReview.css';
import axios from 'axios';
import { useAuth } from '../../security/Components/AuthProvider';
import NavigationBar from '../../Components/NavBar/NavigationBar';
import {useParams} from "react-router-dom";
import Review from "../../Models/Review";
import star_fill from "../../images/Star_fill.svg";
import star_empty from "../../images/Star.svg";

function CustomerViewsSpecificReview() {


    const [review, setReview] = useState<Review>(new Review("", "", 0, false));
    let {reviewId} = useParams();

    const auth = useAuth();

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
            <div className="container review-tile-view-specific-container">
                <p className="review-rating"><b>{filledStars}{emptyStars}</b></p>
                <h2>{review.customerFullName}</h2>
                {review.pinned && <p>Your review is pinned to the landing page</p>}
                <p className="review-date"><b>Date:</b> {review.reviewRating}</p>
                <p className="review-message">{review.message}</p>
            </div>
        </>
    );


}

export default CustomerViewsSpecificReview;
