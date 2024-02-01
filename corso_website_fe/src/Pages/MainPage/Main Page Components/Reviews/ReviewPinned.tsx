import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../../../../security/Components/AuthProvider";
import { ReviewsPinnedTileMainPage } from "./ReviewPinnedTileMainPage";
import { useTranslation } from "react-i18next";
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import 'bootstrap/dist/css/bootstrap.min.css'; // Ensure Bootstrap CSS is imported

function ReviewPinned() {
    const auth = useAuth();
    const { t } = useTranslation();

    const [pinnedReviews, setPinnedReviews] = useState([]);
    useEffect(() => {
        const getPinnedReviews = () => {
            axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews/pinned`, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
                .then(r => {
                    setPinnedReviews(r.data);
                })
                .catch(e => {
                    console.log(e);
                });
        };
        getPinnedReviews();
    }, [auth]);

    const responsive = {
        superLargeDesktop: { breakpoint: { max: 4000, min: 0 }, items: 1 },
        desktop: { breakpoint: { max: 4000, min: 0 }, items: 1 },
        tablet: { breakpoint: { max: 4000, min: 0 }, items: 1 },
        mobile: { breakpoint: { max: 4000, min: 0 }, items: 1 }
    };


    return (
        <div className="container review-mainPage-container">
            <div className="row align-items-center">
                {/* Carousel */}
                <div className="col-lg-6 d-flex justify-content-center">
                    <Carousel
                        responsive={responsive}
                        infinite={true}
                        //autoPlay={true}
                        autoPlaySpeed={3000}
                        keyBoardControl={true}
                        customTransition="transform 300ms ease-in-out"
                        transitionDuration={500}
                        showDots={true}
                        containerClass="carousel-container"
                        dotListClass="custom-dot-list-style"
                    >
                        {pinnedReviews.map((review) => (
                            // @ts-ignore
                            <div key={review.id} className="px-3 align-content-center">
                                <ReviewsPinnedTileMainPage
                                    // @ts-ignore
                                    id={review.id}
                                    // @ts-ignore
                                    customerFullName={review.customerFullName}
                                    // @ts-ignore
                                    reviewDate={review.reviewDate}
                                    // @ts-ignore
                                    message={review.message}
                                    // @ts-ignore
                                    reviewRating={review.reviewRating}
                                    // @ts-ignore
                                    pinned={review.pinned}
                                />
                            </div>
                        ))}
                    </Carousel>
                </div>
                {/* Title and Description */}
                <div className="col-lg-6">
                    <div className="border-review mb-5">
                        <h1 className="title-reviews">{t("testimonials")}</h1>
                        <p className="subtitle-reviews">
                            Here is a default description for the testimonials section.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ReviewPinned;
