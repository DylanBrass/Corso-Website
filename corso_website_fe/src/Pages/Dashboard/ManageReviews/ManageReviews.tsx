import './ManageAllReviews.css';
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import React, {useEffect, useState} from "react";
import axios from "axios";
import star_fill from "../../../ressources/images/Star_fill.svg";
import star_empty from "../../../ressources/images/Star.svg";
import Search_light from "../../../ressources/images/Search_light_white.svg";
import View_light from "../../../ressources/images/View_light.png";
import CrossedOutEye from "../../../ressources/images/Crossed-out_Eye.png";
import {useAuth} from "../../../security/Components/AuthProvider";
import CustomPagination from "../../../Components/Pagination";
import {PuffLoader} from 'react-spinners';
import {Bounce, toast, ToastContainer} from "react-toastify";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


class Reviews {
    reviewId: string;
    customerFullName: string;
    reviewDate: Date;
    message: string;
    reviewRating: number;
    pinned: boolean;
    image: string;


    constructor(reviewId: string, customerFullName: string, reviewDate: Date, message: string, reviewRating: number, pinned: boolean) {
        this.reviewId = reviewId;
        this.customerFullName = customerFullName;
        this.reviewDate = reviewDate;
        this.message = message;
        this.reviewRating = reviewRating;
        this.pinned = pinned;
        this.image = pinned ? View_light : CrossedOutEye;
    }
}

function ManageReviews() {
    const auth: {} = useAuth()
    const {t} = useTranslation();


    const [previousNameSearch, setPreviousNameSearch] = useState<string>('');

    const [reviews, setReviews] = useState<Reviews[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);


    useEffect(() => {
        fetchReviews(currentPage, pageSize);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage, pageSize]);


    const toggleReviewPinned = async (reviewId: string) => {

        document.getElementById(reviewId + "-spinner")?.classList.remove("d-none");
        document.getElementById(reviewId + "-button")?.classList.add("d-none");

        setIsLoading(true)


        await
            axios
                .patch(process.env.REACT_APP_BE_HOST+`api/v1/corso/reviews/${reviewId}`, {
                    pinned: !reviews.find((review) => review.reviewId === reviewId)?.pinned,
                }, {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    }
                })
                .then((response) => {

                    const updatedReviews = reviews.map((review) => {
                        if (review.reviewId === response.data.reviewId) {
                            console.log(response.data)
                            return response.data;
                        }
                        return review;
                    });

                    setReviews(updatedReviews);

                })
                .catch((error) => {
                    
                    if (error.response.status === 401 || error.response.status === 403) {
                        // @ts-ignore
                        auth.authError(error.response.status);

                    }
                    if (error.response.status === 409) {
                        // @ts-ignore
                        swal(t("alerts.warningAlertTitle"), error.response.data.message, "warning")
                    }
                }).finally(() => {
                    
                    document.getElementById(reviewId + "-spinner")?.classList.add("d-none");
                    document.getElementById(reviewId + "-button")?.classList.remove("d-none");
                    setIsLoading(false)
                }
            )
    }

    const fetchReviews = async (pageNumber: number, size: number) => {
        try {

            let reviewUrl = process.env.REACT_APP_BE_HOST+`api/v1/corso/reviews?pageSize=${size}&offset=${(pageNumber - 1) * size}`;

            if (searchTerm.trim()) {
                reviewUrl += `&customerFullName=${searchTerm}`;
            }

            const reviewResponse = await axios.get(reviewUrl, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });


            if (reviewResponse.data.length <= 0) {
                // @ts-ignore
                swal((t("alerts.warningAlertTitle")), (t("alerts.review.noReviewsFound")), "warning")

                setReviews([])
                setTotalPages(0)
                setPage(1)
                setCurrentPage(1)

                return;
            }

            const totalCount = reviewResponse.data[0].totalReviewsMatchingRequest;


            setReviews(reviewResponse.data);
            setTotalPages(Math.ceil(totalCount / size))
        } catch (error) {
            
            setError(t("reviewsAdminPage.errorFetchingReviews"));
            // @ts-ignore
            auth.authError(error.response.status);
        }
    };


    const handlePageChange = (pageNumber: number, size: number) => {
        setPage(pageNumber);
        setPageSize(size);
        fetchReviews(pageNumber, size);
    };


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
    };


    const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (searchTerm.trim() === previousNameSearch) {
            return;
        }

        setPreviousNameSearch(searchTerm)
        // Brings you to the first page when searching
        setPageSize(pageSize);
        setCurrentPage(1);
        fetchReviews(currentPage, pageSize);

    };

    const backButtonHref = "/dashboard";

    return (

        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div className="container review-container">
                <div className="row review-header">
                    <h1 className="review-title">{t("reviewsAdminPage.allReviews")}</h1>
                </div>

                <form className="manage-review-form" onSubmit={handleSearch}>
                    <div className={"form-container w-100"}>

                        <input className="manage-review-input" type="text" name="search"
                               placeholder={t("reviewsAdminPage.searchClientName")} value={searchTerm} onChange={handleChange}/>
                        <button className="manage-review-submit-button" type="submit">
                            <img src={Search_light} alt={"searchBtn"}/>
                        </button>
                    </div>
                </form>


                {
                    error && <div className="row">
                        <div className="col-12"><p className="error">{t("alerts.error")}: {error}</p></div>
                    </div>
                }
                <div className="row">
                    {reviews.map(review => (
                        <div className="col-md-6 col-sm-12 mb-4" key={review.reviewId}>
                            <div className="review-card">

                                <div className="review-info">
                                    <div className="review-box-name row w-100 h-100">
                                        <div className="col-6">
                                            <span className="review-name">{review.customerFullName}</span>
                                        </div>

                                        <div className={"col-2"}/>
                                        <div className={"col-4 text-end"}>

                                            <div className={"float-end d-none"} id={review.reviewId + "-spinner"}>

                                                <PuffLoader
                                                    color="#1e90ff"/>
                                            </div>


                                            <button style={{
                                                border: "none",
                                                background: "none"
                                            }
                                            }
                                                    id={review.reviewId + "-button"}

                                                    className={review.pinned ? " review-eye review-pinned" : "review-eye review-not-pinned"}
                                                    onClick={() => {
                                                        if (isLoading) {
                                                            toast.warn(t("reviewsAdminPage.ongoingPreviousRequest"), {
                                                                position: "top-right",
                                                                autoClose: 5000,
                                                                hideProgressBar: false,
                                                                closeOnClick: true,
                                                                pauseOnHover: true,
                                                                draggable: true,
                                                                progress: undefined,
                                                                theme: "dark",
                                                                transition: Bounce,
                                                            });
                                                            return;
                                                        }
                                                        toggleReviewPinned(review.reviewId).then(r => {
                                                            
                                                        })
                                                    }}>
                                                <img
                                                    src={review.pinned ? View_light : CrossedOutEye}
                                                    alt="View_Light"/>
                                            </button>


                                        </div>
                                    </div>


                                    <div className="row review-content">
                                        <div className="col-12">
                                            <div className={"review-date text-center"}>
                                                <p className="review-date">{review.reviewDate.toString()}</p>
                                            </div>
                                            <p className="review-message">{review.message}</p>
                                            <div className="review-rating text-center"
                                                 title={`Rating: ${review.reviewRating}`}>
                                                {Array.from(Array(review.reviewRating), (e, i) => (
                                                    <img src={star_fill} alt={"Filled Star"} key={i}/>
                                                ))}
                                                {Array.from(Array(5 - review.reviewRating), (e, i) => (
                                                    <img src={star_empty} alt={"Empty Star"} key={i}/>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <div className={"review-pagination-container"}>
                <CustomPagination
                    onPageChange={handlePageChange}
                    pageSize={pageSize}
                    totalPages={totalPages}
                    resetKey={previousNameSearch}
                />

            </div>

            <ToastContainer
                position="top-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="dark"
            />
        </div>
    );

}

export default ManageReviews;
