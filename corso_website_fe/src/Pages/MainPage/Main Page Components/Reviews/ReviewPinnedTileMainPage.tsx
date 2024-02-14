import React from 'react';
import star_fill from "../../../../ressources/images/Star_fill.svg";
import star_empty from "../../../../ressources/images/Star.svg";
import "./ReviewPinned.css";

class Reviews {
    id: string;
    customerFullName: string;
    reviewDate: Date;
    message: string;
    reviewRating: number;
    pinned: boolean;

    constructor(id: string, customerFullName: string, reviewDate: Date, message: string, reviewRating: number, pinned: boolean) {
        this.id = id;
        this.customerFullName = customerFullName;
        this.reviewDate = reviewDate;
        this.message = message;
        this.reviewRating = reviewRating;
        this.pinned = pinned;
    }
}

function ReviewsPinnedTileMainPage(reviews: Reviews) {
    let filledStars = [];
    let emptyStars = [];

    if (reviews.reviewRating !== undefined) {
        for (let i = 0; i < reviews.reviewRating; i++) {
            filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
        }

        for (let i = reviews.reviewRating; i < 5; i++) {
            emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
        }
    }

    return (
        <div className={"review-mainPage-component"}>
            <div className={"review-container"}>
                <div >
                    <h1 className={"review-title"} >{reviews.customerFullName}</h1>
                </div>
                <div>
                    <div>
                        {filledStars}
                        {emptyStars}
                    </div>
                    <p style={{marginTop : "20px"}} className="review-description">{reviews.message}</p>
                </div>
            </div>
        </div>
    );
}

export { ReviewsPinnedTileMainPage, Reviews };
