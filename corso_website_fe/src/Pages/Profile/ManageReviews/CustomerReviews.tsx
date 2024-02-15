import React, {useEffect, useState} from "react";
import axios from "axios";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import ReviewTile from "../../../Components/ReviewTile/ReviewTile";
import './CustomerReviews.css';
import {useTranslation} from "react-i18next";
import swal from "sweetalert";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

interface Review {
    reviewId: string;
    reviewDate: string;
    message: string;
    reviewRating: number;
    pinned: boolean;
}

function CustomerReviews() {
    const [reviews, setReviews] = useState<Review[]>([]);
    const [t] = useTranslation();

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews/user`);
                const fetchedReviews: Review[] = response.data;


                const sortedReviews = fetchedReviews.sort((a, b) => new Date(b.reviewDate).getTime() - new Date(a.reviewDate).getTime());
                setReviews(sortedReviews);

            } catch (error) {
                handleNoReview();
            }
        };

        fetchReviews().then(() => {});
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [t]);

    const handleNoReview = () => {
        swal({
            title: t("alerts.review.noReviewsTitle"),
            text: t("alerts.review.noReviewsMessage"),
            icon: "warning",
            buttons: [t("alerts.review.goBack"), t("alerts.review.createReview")],
            dangerMode: true,
        }).then((willCreateReview) => {
            if (willCreateReview) {
                window.location.href = "/profile/reviews/create";
            } else {
                window.location.href = "/profile";
            }
        });
    };

    const backButtonHref = "/profile";

    return (
        <>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="customer-review-container">
                <h1 className={"title"}>{t("reviews.myReviews")}</h1>
                <div className="customer-review-tiles-container w-75">
                    {reviews.length > 0 ? reviews.map((review, index) => (
                        <ReviewTile
                            index={index}
                            key={review.reviewId}
                            review={{
                                reviewId: review.reviewId,
                                reviewDate: review.reviewDate,
                                message: review.message,
                                reviewRating: review.reviewRating,
                                pinned: review.pinned,
                            }}
                            action={() => {
                                window.location.href = `/profile/reviews/${review.reviewId}`
                            }}
                        />
                    )) : null}
                </div>
            </div>
        </>
    )
}

export default CustomerReviews;