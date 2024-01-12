import React, {useEffect, useState} from "react";
import axios from "axios";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import ReviewTile from "../../Components/ReviewTile/ReviewTile";
import './CustomerReviews.css';

interface Review {
    reviewId: string;
    reviewDate: string;
    message: string;
    reviewRating: number;
    pinned: boolean;
}
function CustomerReviews () {
    const [error, setError] = useState<string | null>(null);
    const [reviews, setReviews] = useState<Review[]>([]);

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/reviews/user');
                const fetchedReviews: Review[] = response.data;

                // Sort reviews by the most recent date
                const sortedReviews = fetchedReviews.sort((a, b) => new Date(b.reviewDate).getTime() - new Date(a.reviewDate).getTime());

                setReviews(sortedReviews);
            } catch (error) {
                console.error('Error fetching reviews:', error);
                setError('Error fetching reviews.');
            }
        };
        fetchReviews();
    }, []);

    return (
        <>
            <NavigationBar/>
            {error && <div className="row"><div className="col-12"><p className="error">Error: {error}</p></div></div>}
            <div className="customer-review-container">
                <h1>My Reviews</h1>
                <div className="customer-review-tiles-container">
                    {reviews.map((review, index) => (
                        <ReviewTile
                            index={index}
                            key={review.reviewId}
                            review={{
                                reviewId: review.reviewId,
                                reviewDate: review.reviewDate,
                                message: review.message,
                                reviewRating: review.reviewRating,
                                pinned: review.pinned,
                                action: () => {
                                    console.log("Service clicked");
                                }
                            }}
                        />
                    ))}
                </div>
            </div>
        </>
    )
}

export default CustomerReviews;