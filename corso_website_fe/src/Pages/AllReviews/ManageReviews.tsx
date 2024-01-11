import './ManageAllReviews.css';
import NavigationBar from "../../Components/NavBar/NavigationBar";
import {useEffect, useState} from "react";
import axios from "axios";

import star_fill from "../../images/Star_fill.svg";

import star_empty from "../../images/Star.svg";
import Search_light from "../../images/Search_light_white.svg";
import View_light from "../../images/View_light.png";
import CrossedOutEye from "../../images/Crossed-out_Eye.png";

import React from "react";
import {useAuth} from "../../security/Components/AuthProvider";


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

function ManageReviews() {
    const auth: {} = useAuth()

    const [reviews, setReviews] = useState<Reviews[]>([]);
    const [error,] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>('');


    /*

    This is for my Choose Viewable Reviews

        const toggleReviewPinned = (reviewId: string) => {
            setReviews(prevReviews =>
                prevReviews.map(review => {
                    if (review.id === reviewId) {
                        return {
                            ...review,
                            pinned: !review.pinned
                        };
                    }
                    return review;
                })
            );
        };
     */


    const getReviews = () => {
        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/reviews",
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                const sortedReviews = r.data.sort((a: any, b: any) => (b.pinned ? 1 : 0) - (a.pinned ? 1 : 0));
                setReviews(sortedReviews);
            })
            .catch(e => {
                console.log(e)
            })


    }


    const getReviewsBySearch = () => {
        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/reviews/search?customerFullName=" + searchTerm,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(r => {
                const searchResults = r.data;
                if (searchResults && searchResults.length > 0) {
                    const sortedReviews = searchResults.sort((a: any, b: any) => (b.pinned ? 1 : 0) - (a.pinned ? 1 : 0));
                    setReviews(sortedReviews);
                } else {
                    getReviews(); // Fetch all reviews if no search results found
                }
                setSearchTerm('');
            })
            .catch(e => {
                console.log(e)
            })
    }

    const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!searchTerm.trim()) {
            getReviews();
        } else {
            getReviewsBySearch();
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
    };


    useEffect(() => {
        getReviews();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);



    return (

        <div>
            <NavigationBar/>
            <div className="container review-container">
                <div className="row review-header">
                    <h1 className="review-title">Reviews</h1>
                </div>

                <form onSubmit={handleSearch}>
                    <div className={"form-container"}>
                        <input type="text" name="search" placeholder="Search Client Name"  value={searchTerm} onChange={handleChange}/>
                        <button type="submit">
                            <img src={Search_light} alt={"searchBtn"}/>
                        </button>
                    </div>
                </form>


                {error && <div className="row">
                    <div className="col-12"><p className="error">Error: {error}</p></div>
                </div>}
                <div className="row">
                    {reviews.map(review => (
                        <div className="col-md-6 col-sm-12 mb-4">
                            <div className="review-card">
                                <div className="review-info">
                                    <div className="review-box-name row w-100 h-100">
                                        <div className="col-6">
                                            <span className="review-name">{review.customerFullName}</span>
                                        </div>

                                        <div className={"col-2"}/>
                                        <div className={"col-4 text-end"}>
                                            {review.pinned && <span className="review-pinned">
                                                  {
                                                      Array.from(Array(review.pinned), (e, i) => {
                                                          return <img src={View_light} alt={"review-pinned"}
                                                                      id={"pinnedId"}/>
                                                      })
                                                  }
                                            </span>
                                            }

                                            {!review.pinned &&
                                                <span className="review-not-pinned">
                                                    {
                                                        Array.from(Array(review.pinned), (e, i) => (
                                                            <img key={i} src={CrossedOutEye} alt={"review-not-pinned"}
                                                                 id={"notPinnedId"}/>
                                                        ))
                                                    }
                                                </span>

                                            }
                                        </div>
                                    </div>


                                    <div className="row review-content">
                                        <div className="col-12">
                                            <div className={"review-date text-center"}>
                                                <p className="review-date">{review.reviewDate.toString()}</p>
                                            </div>
                                            <p className="review-message">{review.message}</p>
                                            <div className={"review-rating text-center"}>
                                                {

                                                    Array.from(Array(review.reviewRating), (e, i) => {
                                                        return <img src={star_fill} alt={"Filled Star"}/>
                                                    })

                                                }

                                                {
                                                    Array.from(Array(5 - review.reviewRating), (e, i) => {
                                                        return <img src={star_empty} alt={"Empty Star"}/>
                                                    })
                                                }

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );

}

export default ManageReviews;