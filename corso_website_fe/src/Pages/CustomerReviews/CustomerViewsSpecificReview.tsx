import React, { useEffect, useState } from 'react';
import './CustomerViewsSpecificReview.css';
import axios from 'axios';
import { useAuth } from '../../security/Components/AuthProvider';
import NavigationBar from '../../Components/NavBar/NavigationBar';
import {useParams} from "react-router-dom";
import Review from "../../Models/Review";

function CustomerViewsSpecificReview() {

    let {reviewId} = useParams();


    const auth = useAuth();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Admin')) {
            window.location.href = 'http://localhost:3000/';
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getReview = async () => {
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
            })
            .catch(err => {
                console.log(err);

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

            })

    }

    useEffect(() => {
        getReview().then(r => console.log("review fetched"));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const [review, setReview] = useState(new Review("", "", 0, false));

    return (
        <>
            <NavigationBar />
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-8">
                        <h1>Rating: {review.reviewRating}</h1>
                        <p>Message: {review.message}</p>
                        <p>Pinned: {review.pinned}</p>
                    </div>

                </div>
            </div>
        </>
    );
}

export default CustomerViewsSpecificReview;
