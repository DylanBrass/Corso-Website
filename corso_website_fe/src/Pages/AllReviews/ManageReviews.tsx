import './ManageAllReviews.css';
import NavigationBar from "../../Components/NavBar/NavigationBar";
import React, {useEffect, useState} from "react";
import axios from "axios";
import star_fill from "../../images/Star_fill.svg";
import star_empty from "../../images/Star.svg";
import Search_light from "../../images/Search_light_white.svg";
import View_light from "../../images/View_light.png";
import CrossedOutEye from "../../images/Crossed-out_Eye.png";
import {useAuth} from "../../security/Components/AuthProvider";
import CustomPagination from "../../Components/Pagination";
import {PuffLoader} from 'react-spinners';
import {Bounce, toast, ToastContainer} from "react-toastify";
import Pagination from "../../Components/Pagination";


class Reviews {
    reviewId: string;
    customerFullName: string;
    reviewDate: Date;
    message: string;
    reviewRating: number;
    pinned: boolean;
    image: string;


    constructor(reviewId: string, customerFullName: string, reviewDate: Date, message: string, reviewRating: number, pinned: boolean, image: string) {
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

    const [reviews, setReviews] = useState<Reviews[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [pageSize, setPageSize] = useState<number>(10); // Updated to use state for pageSize
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [totalPages, setTotalPages] = useState(1);
    const [totalReviews, setTotalReviews] = useState(0);
    const [localPageSize, setLocalPageSize] = useState<number>(pageSize);


    useEffect(() => {
        // Fetch reviews when the component mounts or when there's a change in dependencies
        fetchReviews(currentPage, pageSize);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [currentPage, pageSize]);



    const toggleReviewPinned = async (reviewId: string) => {

        document.getElementById(reviewId + "-spinner")?.classList.remove("d-none");
        document.getElementById(reviewId + "-button")?.classList.add("d-none");

        setIsLoading(true)
        await
            axios
                .patch(`http://localhost:8080/api/v1/corso/reviews/${reviewId}`, {
                    pinned: !reviews.find((review) => review.reviewId === reviewId)?.pinned,
                }, {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken(),
                    }
                })
                .then((response) => {
                    console.log('API Response:', response.data) // Log the API response
                    // Update the pinned status in the local state

                    const updatedReviews = reviews.map((review) => {
                        if (review.reviewId === response.data.reviewId) {
                            return response.data;
                        }
                        return review;
                    });

                    setReviews(updatedReviews);

                })
                .catch((error) => {
                    console.error('API Error:', error);
                    if (error.response.status === 401 || error.response.status === 403) {
                        // @ts-ignore
                        auth.authError(error.response.status);

                    }
                    if (error.response.status === 409) {
                        // @ts-ignore
                        swal("Warning!", error.response.data.message, "warning")
                    }
                }).finally(() => {
                    console.log("Done")
                    document.getElementById(reviewId + "-spinner")?.classList.add("d-none");
                    document.getElementById(reviewId + "-button")?.classList.remove("d-none");
                    setIsLoading(false)
                }
            )
    }


    /*
        const getReviews = () => {
            axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/reviews?pageSize=10",
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
                    // @ts-ignore
                    auth.authError(e.response.status);
                })


        }
            axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews?pageSize=${size}&page=${page}`,



    const getReviews = (page: number, size: number) => {
        axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews?pageSize=${size}&page=${page}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                }
            })
            .then(r => {
                const sortedReviews = r.data.sort((a: any, b: any) => (b.pinned ? 1 : 0) - (a.pinned ? 1 : 0));
                setReviews(sortedReviews);
            })
            .catch(e => {
                console.log(e)
                // @ts-ignore
                auth.authError(e.response.status);
            });
    }

     */



    const fetchReviews = async (pageNumber: number, size: number) => {
        try {
            const countResponse = await axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews/count`, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });

            const totalCount = countResponse.data.count;

            const reviewUrl = `${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews?pageSize=${size}&offset=${(pageNumber - 1) * size}`;
            const reviewResponse = await axios.get(reviewUrl, {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken(),
                },
            });

            const sortedReviews = reviewResponse.data.sort((a: Reviews, b: Reviews) => b.reviewRating - a.reviewRating);

            setReviews(sortedReviews);
            setTotalReviews(totalCount);
        } catch (error) {
            console.error(error);
            setError('Error fetching reviews.');
            // @ts-ignore
            auth.authError(error.response.status);
        }
    };



    const handlePageChange = (newPage: number) => {
        if(newPage > reviews.length){
            return currentPage;
        }else{
            setCurrentPage(newPage);
            fetchReviews(newPage, pageSize);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(e.target.value);
    };


    const getReviewsBySearch = () => {
        axios.get(`${process.env.REACT_APP_BE_HOST}api/v1/corso/reviews/search?customerFullName=${searchTerm}`, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken(),
            },
        })
            .then((response) => {
                const searchResults = response.data;
                if (searchResults && searchResults.length > 0) {
                    const sortedReviews = searchResults.sort((a: any, b: any) => (b.pinned ? 1 : 0) - (a.pinned ? 1 : 0));
                    setReviews(sortedReviews);
                } else {
                    fetchReviews(currentPage, pageSize);
                }
                setSearchTerm('');
            })
            .catch((error) => {
                console.error(error);
                setError('Error searching reviews.');
                // @ts-ignore
                auth.authError(error.response.status);
            });
    };

    const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // Brings you to the first page when searching
        setCurrentPage(1);
        if (!searchTerm.trim()) {
            fetchReviews(currentPage, pageSize);
        } else {
            getReviewsBySearch();
        }
    };



    const handlePageSizeChange = (newSize: number) => {
        setLocalPageSize(newSize);
        setPageSize(newSize);
        setCurrentPage(1);
        fetchReviews(1, newSize);
    };



    return (

        <div>
            <NavigationBar/>
            <div className="container review-container">
                <div className="row review-header">
                    <h1 className="review-title">Reviews</h1>
                </div>

                <form className="manage-review-form" onSubmit={handleSearch}>
                    <div className={"form-container"}>

                        <input className="manage-review-input" type="text" name="search"
                               placeholder="Search Client Name" value={searchTerm} onChange={handleChange}/>
                        <button className="manage-review-submit-button" type="submit">
                            <img src={Search_light} alt={"searchBtn"}/>
                        </button>
                    </div>
                </form>


                {
                    error && <div className="row">
                        <div className="col-12"><p className="error">Error: {error}</p></div>
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

                                                    className={review.pinned ? "review-pinned" : "review-not-pinned"}
                                                    onClick={() => {
                                                        if (isLoading) {
                                                            toast.warn('Your previous request is still ongoing!', {
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
                                                            console.log(r)
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
                    pageSize={localPageSize}
                    totalPages={Math.ceil(totalReviews / localPageSize)}
                    resetKey={null}
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