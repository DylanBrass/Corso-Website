import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useAuth} from "../../../security/Components/AuthProvider";
import axios from "axios";
import {useEffect} from "react";
import "./CreateAdmin.css"
import swal from "sweetalert";
import React from "react";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';



function CreateAdminPage() {
    const auth = useAuth();
    const {t} = useTranslation();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            // @ts-ignore
            window.location.href = "/"
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const addAdmin = async (event: any) => {
        event.preventDefault();
        // @ts-ignore
        await axios.post(process.env.REACT_APP_BE_HOST+"api/v1/corso/auth0/manage/add-admin", {
            username: event.target[0].value,
            email: event.target[1].value,
            password: event.target[2].value
        }, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 201) {

                    swal((t("alerts.admin.adminCreatedTitle")), (t("alerts.admin.adminCreatedMessage")), "success")
                        .then(() => {
                            window.location.href = "/dashboard"
                        })

                }
            })
            .catch(e => {
                swal((t("alerts.error")), e.response.data.message, "error")
                    .then(() => {
                        // @ts-ignore
                        auth.authError(e.response.status)
                    })
            })
    }

    const backButtonHref = "/dashboard";
    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            {
                // @ts-ignore
                //suppress when u get red error on isAuthenticated
                auth.isAuthenticated && auth.userRoles().includes("Admin") &&
                <>
                    <div className={"container create-admin-box mt-5 mb-5"}>
                        <div className={"row w-50"}>
                            <img
                                src={"https://res.cloudinary.com/dszhbawv7/image/upload/v1701224357/base_logo_transparent_background_d3hdde.png"}
                                alt={"Corso Logo"} width={"20%"}/>
                        </div>
                        <div className={"row"}>
                            <div className={"row"} style={{
                                textAlign: "center"
                            }}>
                                <h5>{t("createAdmin.createAdmin")}</h5>
                            </div>
                            <div className={"row form-admin-box w-75 m-auto"}>
                                <form id={"add-admin-form"} onSubmit={addAdmin} style={{

                                }}>
                                    <div className="mt-4">
                                        <div className="row">
                                                <label htmlFor="username" className="form-label">{t("createAdmin.username")}</label>
                                                <input className="form-control mb-2" type="text"
                                                       placeholder={t("createAdmin.username")}/>
                                        </div>
                                        <div className="row">
                                            <label htmlFor="email" className="form-label">{t("createAdmin.email")}</label>
                                                <input className="form-control mb-2" type="text" placeholder={t("createAdmin.email")}/>
                                        </div>
                                        <div className="row">
                                            <label htmlFor="password" className="form-label">{t("createAdmin.password")}</label>
                                                <input className="form-control mb-2" type="password"
                                                       placeholder={t("createAdmin.password")}/>
                                        </div>
                                        <div className="row" style={{
                                            margin: "auto",
                                            width: "70%",
                                            marginTop: "20px",
                                            marginBottom: "120px"
                                        }}>
                                                <button className="btn-style" type="submit">
                                                    {t("createAdmin.addAdmin")}</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </>
            }

        </div>
    );
}

export default CreateAdminPage;
