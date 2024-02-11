import NavigationBar from "../../Components/NavBar/NavigationBar";
import {useAuth} from "../../security/Components/AuthProvider";
import AdminDashboardTile from "../../Components/Dashboard/AdminDashboardTile";
import "./Dashboard.css"
import Cookies from "js-cookie";
import React, {useEffect, useState} from "react";
import axios from "axios";
import { useTranslation } from "react-i18next";

function Dashboard() {

    const [t] = useTranslation();

    const [totalAdmins, setTotalAdmins] = useState(t("load.loading"))

    const [visitors, setVisitors] = useState(t("load.loading"))

    const [inProgressCount, setInProgressCount] = useState(t("load.loading"))
    const [pendingCount, setPendingCount] = useState(t("load.loading"))
    const [completedCount, setCompletedCount] = useState(t("load.loading"))
    const [serviceCount, setServiceCount] = useState(t("load.loading"))
    const [faqCount, setFaqCount] = useState(t("load.loading"))

    const [totalOrders, setTotalOrders] = useState(t("load.loading"))

    const [totalReviews, setTotalReviews] = useState(t("load.loading"))

    const [totalPinnedReviews, setTotalPinnedReviews] = useState(t("load.loading"))

    const [totalOverdueOrders, setTotalOverdueOrders] = useState(t("load.loading"))

    const [totalGalleryImages, setTotalGalleryImages] = useState(t("load.loading"))

    const getTotalReviews = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/reviews/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {

                    setTotalReviews(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }

    const getTotalPinnedReviews = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/reviews/count/pinned", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {

                    setTotalPinnedReviews(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }

    const getTotalAdmins = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/auth0/manage/get-total-admins", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {

                    setTotalAdmins(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.status)
            })
    }

    const getTotalOrders = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/all/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {

                    setTotalOrders(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }

    const getVisitors = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/auth0/manage/get-stats-last-30-days", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {

                    setVisitors(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.status)
            })
    }

    const getInProgressCount = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/manage/count", {
            params: {
                status: "IN_PROGRESS"
            },
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setInProgressCount(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    };

    const getPendingCount = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/manage/count", {
            params: {
                status: "PENDING"
            },
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setPendingCount(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    };
    const getCompletedCount = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/manage/count", {
            params: {
                status: "COMPLETED"
            },
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setCompletedCount(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    };

    const getServicesCount = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/services/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setServiceCount(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    }


    const getFaqCount = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/faqs/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setFaqCount(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    }


    const getTotalOverdueOrders = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/manage/overdue/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setTotalOverdueOrders(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    }

    const getTotalGalleryImages = async () => {
        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/galleries/carousel/count", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 200) {
                    setTotalGalleryImages(response.data);
                }
            })
            .catch(error => {

                // @ts-ignore
                auth.authError(error.response.status);
            });
    }


    const auth = useAuth()

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "/"
        }

        getTotalAdmins().then(r => {

        })

        getVisitors().then(r => {

        })

        getInProgressCount().then(() => {
        });

        getPendingCount().then(() => {
        });

        getCompletedCount().then(() => {
        });

        getServicesCount()

        getTotalOrders().then(r => {

        })

        getTotalReviews().then(r => {

        })

        getTotalPinnedReviews().then(r => {

        })

        getFaqCount().then(r => {

        })

        getTotalOverdueOrders().then(r => {

        })

        getTotalGalleryImages().then(r => {

        })

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    return (
        <div style={{
            textAlign: "center"
        }}>
            <NavigationBar/>
            <h1 className={"w-100"}>{t("dashboardPage.welcomeBack")} {Cookies.get("username")}</h1>

            <div className={"admin-dashboard container"} style={{}}>
                <div className={"row d-flex align-items-stretch mt-0 mt-lg-4"}>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile
                            title1={completedCount.toString()}
                            title2={""}
                            text1={t("dashboardPage.completedOrders")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon1_arlmbi.png"}
                            action={() => {

                                window.location.href = "/orders/completed"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={inProgressCount.toString()}
                            title2={""}
                            text1={t("dashboardPage.currentOrders")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon2_upzhcv.png"}
                            action={() => {

                                window.location.href = "/orders/current"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={pendingCount.toString()}
                            title2={""}
                            text1={t("dashboardPage.clientRequests")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon3_fv4ddv.png"}
                            action={() => {

                                window.location.href = "/orders/pending"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-0 mt-lg-4"}>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile
                            title1={totalOrders.toString()}
                            title2={""}
                            text1={t("dashboardPage.allOrders")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267713/icon4_v5phq3.png"}
                            action={() => {

                                window.location.href = "/orders/all"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={totalOverdueOrders.toString()}
                            title2={""}
                            text1={t("dashboardPage.lateOrders")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon5_tgqojw.png"}
                            action={() => {

                                window.location.href = "/orders/overdue"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>


                        <AdminDashboardTile
                            title1={""}
                            title2={visitors.toString()}
                            text1={t("dashboardPage.viewMainPage")}
                            text2={t("dashboardPage.uniqueVisitorsNumber")}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon9_ehmde3.png"}
                            action={() => {
                                window.location.href = "/"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-0 mt-lg-4"}>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile
                            title1={totalGalleryImages.toString()}
                            title2={""}
                            text1={t("dashboardPage.galleryImages")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon7_vftm9q.png"}
                            action={() => {

                                window.location.href = "/carousel/order"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile title1={t("dashboardPage.reports")} text1={""} title2={""} text2={""} icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon13_pxn61t.png"}
                                            action={() => {
                                                window.location.href = "/reports"
                                            }}/>
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={faqCount.toString()}
                            title2={""}
                            text1={t("dashboardPage.currentFAQs")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon6_wbpr29.png"}
                            action={() => {
                                window.location.href = "manage/faqs"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-0 mt-lg-4"}>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile
                            title1={""}
                            title2={""}
                            text1={t("dashboardPage.createTrackingNumber")}
                            text2={t("dashboardPage.forExternalOrders")}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon10_c7xbqz.png"}
                            action={() => {
                                window.location.href = "/create/orders/external"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={serviceCount.toString()}
                            title2={""}
                            text1={t("dashboardPage.servicesOffered")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon11_pprfnf.png"}
                            action={() => {

                                window.location.href = "/services"
                            }}
                        />
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>
                        <AdminDashboardTile
                            title1={totalAdmins.toString()}
                            title2={""}
                            text1={t("dashboardPage.adminAccounts")}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon12_fpyhus.png"}
                            action={() => {
                                window.location.href = "/create/admin"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-0 mt-lg-4"}>
                    <div className={"col-12 col-lg-4 mt-lg-0"}>
                    </div>
                    <div className={"col-12 mt-3 col-lg-4 mt-lg-0"}>

                        <AdminDashboardTile
                            title1={totalReviews.toString()}
                            title2={totalPinnedReviews + "/6"}
                            text1={t("dashboardPage.totalTestimonies")}
                            text2={t("dashboardPage.pinnedTestimonies")}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon8_jbbxv9.png"}
                            action={() => {

                                window.location.href = "/manage/testimonies"
                            }}
                        />
                    </div>
                    <div className={"col-12 col-lg-4 mt-lg-0"}>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default Dashboard;