import React from "react";
import './ReviewTile.css';
import star_fill from "../../images/Star_fill.svg";
import star_empty from "../../images/Star.svg";

export {};

export class Review {
    reviewId: string;
    reviewDate: string;
    message: string;
    reviewRating: number;
    pinned: boolean;
    action: () => void;

    constructor(reviewId: string, reviewDate: string, message: string, reviewRating: number, pinned: boolean, action: () => void) {
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.message = message;
        this.reviewRating = reviewRating;
        this.pinned = pinned;
        this.action = action;
    }
}

function ReviewTile({ review, index }: { review: Review, index: number }) {
    const filledStars = [];
    const emptyStars = [];

    for (let i = 0; i < review.reviewRating; i++) {
        filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
    }

    for (let i = review.reviewRating; i < 5; i++) {
        emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
    }

    return (
        <div className="customer-review-tile">
            <p className="review-number"><b>Rating: # {index + 1}</b></p>
            <p className="review-date"><b>Date:</b> {review.reviewDate}</p>
            <p className="review-message">{review.message}</p>
            <p className="review-rating">{filledStars}{emptyStars}</p>
        </div>
    );
}

export default ReviewTile;