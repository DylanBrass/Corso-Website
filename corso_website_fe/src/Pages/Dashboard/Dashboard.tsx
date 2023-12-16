import NavigationBar from "../../Components/NavigationBar";
import {useAuth} from "../../security/Components/AuthProvider";
import AdminDashboardTile from "../../Components/AdminDashboardTile";
import "./Dashboard.css"
import Cookies from "js-cookie";
import {useEffect, useState} from "react";
import axios from "axios";

function Dashboard() {

    const [totalAdmins, setTotalAdmins] = useState(0)

    const [visitors, setVisitors] = useState(0)

    const getTotalAdmins = async () => {
        axios.get("http://localhost:8080/api/v1/corso/auth0/manage/get-total-admins", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    console.log(r.data)
                    setTotalAdmins(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }


    const getVisitors = async () => {
        axios.get("http://localhost:8080/api/v1/corso/auth0/manage/get-stats-last-30-days", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    console.log(r.data)
                    setVisitors(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }



    const auth = useAuth()

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "/"
        }

        getTotalAdmins().then(r => {
            console.log(r)
        })

        getVisitors().then(r => {
            console.log(r)
        })

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])
    return (
        <div style={{
        }}>
            <NavigationBar/>
            <h1>Welcome Back {Cookies.get("username")}</h1>

            <div className={"admin-dashboard container"} style={{}}>
                <div className={"row d-flex align-items-stretch mt-4"}>
                    <div className={"col-4"}>
                        <AdminDashboardTile
                            title1={"61"}
                            title2={""}
                            text1={"Completed Orders"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon1_arlmbi.png"}
                            action={() => {
                                console.log("test")
                                window.location.href = "/orders/completed"
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"2"}
                            title2={""}
                            text1={"Current Orders"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon2_upzhcv.png"}
                            action={() => {
                                console.log("test")
                                window.location.href = "/orders/current"
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"1"}
                            title2={""}
                            text1={"Client Requests"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267714/icon3_fv4ddv.png"}
                            action={() => {
                                console.log("test")
                                window.location.href = "/orders/pending"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-4"}>
                    <div className={"col-4"}>
                        <AdminDashboardTile
                            title1={"310"}
                            title2={""}
                            text1={"All Orders"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267713/icon4_v5phq3.png"}
                            action={() => {
                                console.log("test")
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"3"}
                            title2={""}
                            text1={"Late Orders"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon5_tgqojw.png"}
                            action={() => {
                                console.log("test")
                            }}
                        />
                    </div>
                    <div className={"col-4"}>


                        <AdminDashboardTile
                            title1={""}
                            title2={visitors.toString()}
                            text1={"View Main Page"}
                            text2={"Unique Visitors in the last 30 days"}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon9_ehmde3.png"}
                            action={() => {
                                window.location.href = "/"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-4"}>
                    <div className={"col-4"}>
                        <AdminDashboardTile
                            title1={"4"}
                            title2={""}
                            text1={"Gallery Images"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon7_vftm9q.png"}
                            action={() => {
                                console.log("test")
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile title1={"Reports"} text1={""} title2={""} text2={""} icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon13_pxn61t.png"}
                                            action={() => {
                                                console.log("test")
                                            }}/>
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"6"}
                            title2={""}
                            text1={"Current FAQs"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon6_wbpr29.png"}
                            action={() => {
                console.log("test")
            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-4"}>
                    <div className={"col-4"}>
                        <AdminDashboardTile
                            title1={""}
                            title2={""}
                            text1={"Create Tracking Number"}
                            text2={"for External Orders"}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon10_c7xbqz.png"}
                            action={() => {
                                window.location.href = "/create/orders/external"
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"10"}
                            title2={""}
                            text1={"Service Offered"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon11_pprfnf.png"}
                            action={() => {
                                console.log("test")
                            }}
                        />
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={totalAdmins.toString()}
                            title2={""}
                            text1={"Admin Accounts"}
                            text2={""}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon12_fpyhus.png"}
                            action={() => {
                                window.location.href = "/create/admin"
                            }}
                        />
                    </div>
                </div>
                <div className={"row d-flex align-items-stretch mt-4 mb-5"}>
                    <div className={"col-4"}>
                    </div>
                    <div className={"col-4"}>

                        <AdminDashboardTile
                            title1={"2"}
                            title2={"6/6"}
                            text1={"Total Testimonies"}
                            text2={"Pinned Testimonies"}
                            icon={"https://res.cloudinary.com/dszhbawv7/image/upload/v1702267712/icon8_jbbxv9.png"}
                            action={() => {
                                console.log("test")
                            }}
                        />
                    </div>
                    <div className={"col-4"}>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default Dashboard;
