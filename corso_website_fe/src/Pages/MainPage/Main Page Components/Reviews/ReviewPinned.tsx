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
        superLargeDesktop: {
            breakpoint: { max: 4000, min: 3000 },
            items: 1
        },
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 1
        },
        tablet: {
            breakpoint: { max: 1024, min: 464 },
            items: 1
        },
        mobile: {
            breakpoint: { max: 464, min: 0 },
            items: 1
        }
    };


    return (
        <div style={{padding: "10px 10%"}} className="review-mainPage-container">
            <div style={{gap: "30px"}} className="d-lg-flex flex-lg-nowrap flex-row justify-content-center align-items-center">

                {/* Title and Description */}
                <div className="testimonialHeaderContainer">
                    <div className="border-review mb-5">
                        <h1 className="title-reviews">{t("mainPage.testimonials")}</h1>
                        <p className="subtitle-reviews">
                            {t("mainPage.testimonialsBlurb")}
                        </p>
                    </div>
                </div>

                {/* Carousel */}
                <div className="w-lg-50">
                    <Carousel
                        responsive={responsive}
                        infinite={true}
                        autoPlay={true}
                        autoPlaySpeed={3000}
                        keyBoardControl={true}
                        customTransition="transform 300ms ease-in-out"
                        transitionDuration={500}
                        arrows={false}
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

            </div>
        </div>
    );
}

export default ReviewPinned;
