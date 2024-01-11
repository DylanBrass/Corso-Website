import React, {useEffect, useState} from "react";
import {useAuth} from "../../security/Components/AuthProvider";
import {Reviews} from "./ReviewPinnedTileMainPage";
import axios from "axios";
import {ReviewsPinnedTileMainPage} from "./ReviewPinnedTileMainPage";
import './ReviewPinned.css'


function ReviewPinned() {

    const auth = useAuth()

    const [pinnedReviews, setPinnedReviews] = useState([Reviews])
    const getPinnedReviews = () => {
        axios.get("http://localhost:8080/api/v1/corso/reviews/pinned",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                setPinnedReviews(r.data)
            })
            .catch(e => {
                console.log(e)
            })
    }


    useEffect(() => {
        getPinnedReviews()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    return (
        <div className={"testimonies-container w-100 testimonies-box"}>
            <div className={"testimonies-main-box w-100"}>

                <div className={"testimonies-title-box d-none d-md-flex"}>
                        <h1 className={"testimonials-title"}>Testimonials</h1>
                </div>

                <div className={"row"}>
                    {pinnedReviews.map((value) => (
                        <div className={"col-12 col-md-4 review-tile-box"}>
                            <ReviewsPinnedTileMainPage
                                //@ts-ignore
                                key={value.id}
                                //@ts-ignore
                                id={value.id}
                                //@ts-ignore
                                customerFullName={value.customerFullName}
                                //@ts-ignore
                                reviewDate={value.reviewDate}
                                //@ts-ignore
                                message={value.message}
                                //@ts-ignore
                                reviewRating={value.reviewRating}
                                //@ts-ignore
                                pinned={value.pinned}
                            />
                        </div>
                    ))}
                </div>
            </div>

        </div>


    );
}

export default ReviewPinned
