import React from "react";
import './ReviewTile.css';
import star_fill from "../../ressources/images/Star_fill.svg";
import star_empty from "../../ressources/images/Star.svg";
import {useTranslation} from "react-i18next";

export class Review {
    reviewId: string;
    reviewDate: string;
    message: string;
    reviewRating: number;
    pinned: boolean;

    constructor(reviewId: string, reviewDate: string, message: string, reviewRating: number, pinned: boolean) {
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.message = message;
        this.reviewRating = reviewRating;
        this.pinned = pinned;
    }
}

function ReviewTile({ review, index, action }: { review: Review, index: number, action: () => void  }) {
    const filledStars = [];
    const emptyStars = [];
    const [t] = useTranslation();

    for (let i = 0; i < review.reviewRating; i++) {
        filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
    }

    for (let i = review.reviewRating; i < 5; i++) {
        emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
    }

    return (
        <div className="customer-review-tile w-100" onClick={action}>
            <p className="review-number"><b>{t("reviews.review")}: # {index + 1}</b></p>
            <p className="review-date"><b>Date:</b> {review.reviewDate}</p>
            <p className="review-message">{review.message}</p>
            <p className="review-rating">{filledStars}{emptyStars}</p>
        </div>
    );
}

export default ReviewTile;