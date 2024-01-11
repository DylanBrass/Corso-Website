import React from 'react';
import './ReviewPinnedTileMainPage.css';
import star_fill from "../../images/Star_fill.svg";
import star_empty from "../../images/Star.svg";


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

    if (reviews.reviewRating !== undefined && typeof reviews.reviewRating === 'number') {
        for (let i = 0; i < reviews.reviewRating; i++) {
            filledStars.push(<img key={`filled-${i}`} src={star_fill} alt="Filled Star" />);
        }

        for (let i = reviews.reviewRating; i < 5; i++) {
            emptyStars.push(<img key={`empty-${i}`} src={star_empty} alt="Empty Star" />);
        }
    }

    return (
                                <div className="testimonies-info">
                                    <div className="testimonies-box-name row tex">
                                        <div className="col-12 text-center p-2 m-2">
                                            <span className="testimonies-name">{reviews.customerFullName}</span>
                                        </div>
                                        <div className="testimonies-content">
                                            <div className="col-12">
                                                <p className="testimonies-message">{reviews.message}</p>
                                                <div className={"testimonies-rating text-center m-3"}>
                                                    {filledStars}
                                                    {emptyStars}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
    );
}


export {ReviewsPinnedTileMainPage, Reviews}
