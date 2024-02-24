import axios from "axios";
import Cookies from "js-cookie";
import "./ModifyProfile.css";
import React, {useEffect, useState} from "react";
import swal from "sweetalert";
import {usePlacesWidget} from "react-google-autocomplete";
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useTranslation} from "react-i18next";
import '../../globalStyling/globalStyling.css';
import BackButton from "../../../Components/BackButton";



function AddCustomerInformation() {
    const auth = useAuth();


    const {t} = useTranslation("translation");

    const [hasProfile, setHasProfile] = useState(false);

    const [isVerified, setIsVerified] = useState(false);


    const [isLoading, setIsLoading] = useState(false);

    const {ref} = usePlacesWidget({
        apiKey: process.env.REACT_APP_MAPS_API_KEY,
        onPlaceSelected: (place) => {
            try {
                document.getElementById("city")!.setAttribute("value", place.address_components[2].long_name)
                document.getElementById("postalCode")!.setAttribute("value", place.address_components[place.address_components.length - 1].long_name)

            } catch (e) {

            }
        },
        options: {
            types: ["address"],
            componentRestrictions: {country: "ca"},
        }
    })


    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated) {
            window.location.href = "/";
        }


        checkIfProfileExists().then(r => {

        })


        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const checkIfProfileExists = async () => {
        await axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers?simpleCheck=true", {
            headers: {
                // @ts-ignore
                "X-XSRF-TOKEN": auth.getXsrfToken(),
            }
        }).then(r => {

            if (r.status === 200) {
                setHasProfile(true);

                axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                }).then(r => {
                    if (r.status === 200) {

                        setIsVerified(r.data.verified)

                        if(r.data.verified){
                            Cookies.set("email", r.data.email);
                        }else {
                            // @ts-ignore
                            auth.getUserInfo()
                        }

                        document.getElementById("name")!.setAttribute("value", r.data.name);
                        document.getElementById("email")!.setAttribute("value", r.data.email);
                        document.getElementById("address")!.setAttribute("value", r.data.address);
                        document.getElementById("phone")!.setAttribute("value", r.data.phone);
                        document.getElementById("postalCode")!.setAttribute("value", r.data.postalCode);
                        document.getElementById("city")!.setAttribute("value", r.data.city)
                        if (r.data.apartmentNumber !== null)
                            document.getElementById("apartmentNumber")!.setAttribute("value", r.data.apartmentNumber);
                    }
                }).catch(e => {

                    }
                )
            } else {
                setHasProfile(false);
                document.getElementById("name")!.setAttribute("value", Cookies.get("username")!);
                document.getElementById("email")!.setAttribute("value", Cookies.get("email")!);
            }
        }).catch(e => {

                setHasProfile(false);
                setIsVerified(true)
                document.getElementById("name")!.setAttribute("value", Cookies.get("username")!);
                document.getElementById("email")!.setAttribute("value", Cookies.get("email")!);
            }
        )


    }


    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setIsLoading(true);
        const phoneInput = document.getElementById("phone") as HTMLInputElement;
        const phoneValue = phoneInput.value;
        const phonePattern = /^\+?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/im;
        if (phoneValue !== "" && !phonePattern.test(phoneValue)) {
            swal(t("alerts.customerInfo.invalidPhoneNumberAlertTitle"), t("alerts.customerInfo.invalidPhoneNumberAlertMessage") +
                "(123) 456-7890\n" +
                "(123)456-7890\n" +
                "123-456-7890\n" +
                "123.456.7890\n" +
                "1234567890\n" +
                "+31636363634\n" +
                "075-63546725", "error")
            return;
        }

        const postalCodeInput = document.getElementById("postalCode") as HTMLInputElement;
        const postalCodeValue = postalCodeInput.value;
        const canadianPostalCodePattern = /^[ABCEGHJ-NPRSTVXY]\d[ABCEGHJ-NPRSTV-Z][ -]?\d[ABCEGHJ-NPRSTV-Z]\d$/i

        if (!canadianPostalCodePattern.test(postalCodeValue)) {
            swal(t("alerts.customerInfo.invalidPostalCodeAlertTitle"), t("alerts.customerInfo.invalidPostalCodeAlertMessage"), "error");
            return;
        }


        const data = new FormData(event.currentTarget);
        const body = {
            name: data.get("username"),
            email: data.get("email"),
            address: data.get("address"),
            phone: data.get("phone"),
            postalCode: data.get("postalCode"),
            city: data.get("city"),
            country: data.get("country"),
            apartmentNumber: data.get("apartmentNumber")
        };
        if (hasProfile) {
            axios.put(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", body, {
                headers: {
                    // @ts-ignore
                    "X-XSRF-TOKEN": auth.getXsrfToken(),
                },
            }).then(r => {
                if (r.status === 200) {
                    if (Cookies.get("email") !== r.data.email)
                        swal(t("alerts.profile.profileUpdatedSuccessAlertTitle"), t("alerts.profile.profileSuccessAlertMessage"), "success").then(r => {
<<<<<<< Updated upstream
                            window.location.replace(document.referrer);
                        })
                    else swal(t("alerts.profile.profileUpdatedSuccessAlertTitle"), "", "success").then(r => {
                        window.location.replace(document.referrer);
=======
                            window.location.href = "/profile";
                        })
                    else swal(t("alerts.profile.profileUpdatedSuccessAlertTitle"), "", "success").then(r => {
                        window.location.href = "/profile";
>>>>>>> Stashed changes
                    })
                    Cookies.set("username", r.data.name);

                    if (r.data.verified) {
                        Cookies.set("email", r.data.email);
                    }
                }
            })
                .catch(e => {
                    let errors = "";

                    if (e.response.data.errors !== undefined) {
                        // @ts-ignore
                        e.response.data.errors.forEach((error: any) => {
                            errors += error.defaultMessage + "\n";
                        })
                    }


                    swal(t("alerts.error"), t("alerts.errorAlertMessage") + errors, "error").then(r => {
                        // @ts-ignore
                        auth.authError(e.response.status)
                    })
                }).finally(() => {
                setIsLoading(false);
                // @ts-ignore
                auth.getUserInfo();
            })
        } else {
            axios
                .post(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", body, {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                })
                .then((r: any) => {
                    if (r.status === 201) {

                        Cookies.set("username", r.data.name);
                        if (r.data.verified) {
                            Cookies.set("email", r.data.email);
                        }
                        swal(t("alerts.profile.profileCreatedSuccessAlertTitle"), t("alerts.profile.profileSuccessAlertMessage"), "success")
                            .then(r => {
<<<<<<< Updated upstream
                                window.location.replace(document.referrer);
=======
                                window.location.href = "/profile";
>>>>>>> Stashed changes
                            })
                        setHasProfile(true);
                    }
                }).catch(e => {

                let errors = "";

                if (e.response.data.errors !== undefined) {
                    // @ts-ignore
                    e.response.data.errors.forEach((error: any) => {
                        errors += error.defaultMessage + "\n";
                    })
                }


                swal(t("alerts.error"), t("alerts.errorAlertMessage") + errors, "error").then(r => {
                    // @ts-ignore
                    auth.authError(e.response.status)
                })
            }).finally(() => {
                setIsLoading(false);
                // @ts-ignore
                auth.getUserInfo();
            })
        }


    };


    const deleteProfile = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const response = await axios.delete(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", {
                headers: {
                    // @ts-ignore
                    "X-XSRF-TOKEN": auth.getXsrfToken(),
                }
            });


            if (response.status === 204) {

                Cookies.remove("username");
                Cookies.remove("email");

                await swal(t("alerts.profile.profileDeletedSuccessAlertTitle"), t("alerts.profile.profileDeletedSuccessAlertMessage"), "success");

                axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/security/user-info", {

                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                }).then(r => {

                    Cookies.set("username", r.data.username);
                    Cookies.set("email", r.data.email);

                    document.getElementById("name")!.setAttribute("value", r.data.username === undefined ? "" : r.data.username);
                    document.getElementById("email")!.setAttribute("value", r.data.email === undefined ? "" : r.data.email);
                    document.getElementById("address")!.setAttribute("value", "");
                    document.getElementById("phone")!.setAttribute("value", "");
                    document.getElementById("postalCode")!.setAttribute("value", "");
                    document.getElementById("city")!.setAttribute("value", "");
                    document.getElementById("apartmentNumber")!.setAttribute("value", "");
                }).catch(e => {

                        if (e.response.status === 401 || e.response.status === 403) {
                            // @ts-ignore
                            auth.authError(e.response.status);
                        } else if (e.response.status === 404) {
                            swal(t("alerts.profile.noProfileFoundAlertTitle"), t("alerts.profile.noProfileFoundAlertMessage"), "warning")
                        } else {
                            swal(t("alerts.error"), e.response.data.message, "error")
                        }
                    }
                )
                setHasProfile(false);

                setIsVerified(true)
            } else {
                swal(t("alerts.error"), t("alerts.somethingWentWrong"), "error");
            }
        } catch (e: any) {
            if (e.response.status === 401 || e.response.status === 403) {
                // @ts-ignore
                auth.authError(e.response.status);
            } else if (e.response.status === 404) {
                swal(t("alerts.profile.noProfileFoundAlertTitle"), t("alerts.errorNoInformationSaved"), "warning")
            } else {
                swal(t("alerts.error"), e.response.data.message, "error")
            }
        }
    };

    const backButtonHref = "/profile";
    return (
        <div className={"h-100"} style={{
            backgroundColor: "#F3F3F3",
            minHeight: "100vh",
            overflow: "hidden",
        }}>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className="profile-page">
                <div className="profile-box mt-5 mb-5">
                    <h1>{t("profilePage.profile")}</h1>

                    <p className={"text-center fs-4 m-3"}>
                        {t("profilePage.profileInformationBlurb")}
                    </p>

                    <form onSubmit={handleSubmit} className="container">
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.name")}:
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()
                                }} type="text" name="username" className="form-control mb-2 "
                                       placeholder={t("customerInfo.name")}
                                       id={"name"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label className={"mt-2"}>
                                {t("customerInfo.email")} {isVerified ? <span className={"col-1 m-auto"}
                                                                              style={{color: "green"}}>({t("email.verifiedEmail")})</span> :
                                <span className={"mt-auto mb-auto text-end"}
                                      style={{color: "red"}}>({t("email.unverifiedEmail")})</span>}:
                                <div className={"d-flex flex-row"}>
                                    <div className={"col-10"}>
                                        <input
                                            onChange={
                                                (e) => {
                                                    if (e.target.value !== Cookies.get("email"))
                                                        setIsVerified(false)

                                                }
                                            }
                                            onKeyDown={(e) => {
                                                e.key === 'Enter' && e.preventDefault()
                                            }} type="text" name="email" className="form-control mb-2 col-10"
                                            placeholder={t("customerInfo.email")}
                                            id={"email"}
                                            required={true}/>
                                    </div>
                                    {
                                        isVerified ?
                                            <div className={"row col-2 text-end ms-auto"}>
                                                <h1 className={"text-end"} style={{color: "green"}}>✓</h1>

                                            </div>
                                            :
                                            <div className={"row col-2 text-end ms-auto"}>
                                                <h1 className={"text-end"} style={{color: "red"}}>✗</h1>

                                            </div>

                                    }
                                </div>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.address")}:
                                {/*// @ts-ignore*/}
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()

                                    // @ts-ignore
                                }} ref={ref} type="text" name="address" className="form-control mb-2"
                                       placeholder={t("customerInfo.address")}
                                       id={"address"}
                                       required={true}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.postalCode")}:
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()
                                }} type="text" name="postalCode" className="form-control mb-2"
                                       placeholder={t("customerInfo.postalCode")} required={true}
                                       id={"postalCode"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.city")}:
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()
                                }} type="text" name="city" className="form-control mb-2"
                                       placeholder={t("customerInfo.city")}
                                       required={true} id={"city"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.apartmentNumber")}:
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()
                                }} type="text" name="apartmentNumber" className="form-control mb-2"
                                       placeholder={t("customerInfo.apartmentNumber")} id={"apartmentNumber"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                {t("customerInfo.phoneNumber")}:
                                <input onKeyDown={(e) => {
                                    e.key === 'Enter' && e.preventDefault()
                                }} type="text" name="phone" className="form-control mb-2"
                                       placeholder={t("customerInfo.phoneNumber")}
                                       id={"phone"}/>
                            </label>
                        </div>
                        <div className="row w-25 m-auto"
                             style={{margin: "auto", width: "50%", marginTop: "20px", marginBottom: "120px"}}>
                            <button id={"save-btn"}
                                    type="submit"
                                    className="btn btn-style"
                                    style={{
                                        margin: "auto",
                                        marginBottom: "20px",
                                    }}
                                    disabled={isLoading}
                            >
                                {t("submitButton")}
                            </button>
                        </div>
                    </form>

                    <form onSubmit={deleteProfile} className="container">
                        <div className="row w-50 m-auto"
                             style={{margin: "auto", marginTop: "20px", marginBottom: "120px"}}>
                            <button
                                type="submit"
                                className="btn btn-style-red mb-2"
                                id={"delete-btn"}
                                disabled={!hasProfile}
                            >
                                {t("profilePage.deleteInformationButton")}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}


export default AddCustomerInformation
