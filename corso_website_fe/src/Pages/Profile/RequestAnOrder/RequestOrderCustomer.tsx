import React, {useEffect, useState} from 'react';
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import axios from "axios";
import swal from "sweetalert";
import Cookies from "js-cookie";
import {usePlacesWidget} from "react-google-autocomplete";
import {useTranslation} from "react-i18next";
import './RequestOrderCustomer.css';
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import i18n from "i18next";
import '../../globalStyling/globalStyling.css';
import BackButton from "../../../Components/BackButton";

function RequestOrder() {
    const {t} = useTranslation("translation");

    const auth = useAuth();
    const [allServices, setAllServices] = useState([]);
    const [selectedServiceId, setSelectedServiceId] = useState('');

    const [minDate, setMinDate] = useState("");
    const [email, setEmail] = useState(Cookies.get("email"))
    const [isVerified, setIsVerified] = useState(false);
    const [unverifiedEmail, setUnverifiedEmail] = useState("");

    useEffect(() => {
        const today = new Date();
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        const minDateValue = tomorrow.toISOString().split('T')[0];
        setMinDate(minDateValue);
    }, []);


    useEffect(() => {
        const fetchServices = async () => {
            try {
                const response = await axios.get(process.env.REACT_APP_BE_HOST + 'api/v1/corso/services/visible');
                setAllServices(response.data);

            } catch (error) {

                // @ts-ignore
                setError(error.message);
            }
        };
        fetchServices();
    }, []);

    const checkIfProfileExists = async () => {
        await axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers?simpleCheck=true", {
            headers: {
                // @ts-ignore
                "X-XSRF-TOKEN": auth.getXsrfToken(),
            }
        }).then(r => {

            if (r.status === 200) {
                axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/customers", {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                }).then(r => {
                    if (r.status === 200) {

                        if (r.data.verified === false) {
                            console.log("User is not verified")
                            setUnverifiedEmail(r.data.email);
                        }

                        document.getElementById("customerFullName")!.setAttribute("value", r.data.name);
                        document.getElementById("customerAddress")!.setAttribute("value", r.data.address);
                        document.getElementById("customerPhone")!.setAttribute("value", r.data.phone);
                        document.getElementById("customerPostalCode")!.setAttribute("value", r.data.postalCode);
                        document.getElementById("customerCity")!.setAttribute("value", r.data.city)
                        if (r.data.apartmentNumber !== null)
                            document.getElementById("apartmentNumber")!.setAttribute("value", r.data.apartmentNumber);
                    }
                }).catch(e => {

                    }
                )
            } else {
                swal({
                    title: t("profilePage.profileInformation"),
                    text: t("alerts.profile.noProfileInformation"),
                    icon: "warning",
                    // @ts-ignore
                    buttons: [t("profilePage.proceedAnyway"), t("profilePage.goToProfile")]
                }).then((willProceed) => {
                    if (willProceed) {
                        window.location.href = "/profile/edit";
                    }
                });
                document.getElementById("customerFullName")!.setAttribute("value", Cookies.get("username")!);
            }
        }).catch(e => {
                swal({
                    title: t("profilePage.profileInformation"),
                    text: t("alerts.profile.noProfileInformation"),
                    icon: "warning",
                    // @ts-ignore
                    buttons: [t("profilePage.proceedAnyway"), t("profilePage.goToProfile")]
                }).then((willProceed) => {
                    if (willProceed) {
                        window.location.href = "/profile/edit";
                    }
                });
                document.getElementById("customerFullName")!.setAttribute("value", Cookies.get("username")!);
            }
        )

    }


    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Customer')) {
            window.location.href = '/';
            return
        }

        //@ts-ignore
        auth.getUserInfo().then(() => {
            setEmail(Cookies.get("email"))
            setIsVerified(Cookies.get("isVerified") === "true")
        })

        checkIfProfileExists().then(() => {

        })

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const {ref} = usePlacesWidget({
        apiKey: process.env.REACT_APP_MAPS_API_KEY,
        onPlaceSelected: (place) => {
            try {
                document.getElementById("customerCity")!.setAttribute("value", place.address_components[2].long_name)
                document.getElementById("customerPostalCode")!.setAttribute("value", place.address_components[place.address_components.length - 1].long_name)

            } catch (e) {

            }
        },
        options: {
            types: ["address"],
            componentRestrictions: {country: "ca"},
        }
    })

    const requestOrder = () => {
        if (isVerified
            || unverifiedEmail === ""
        ) {
            swal({
                title: t("alerts.email.confirmEmail"),
                text: t("alerts.email.confirmEmailMessage", {email: email}),
                icon: "info",
                // @ts-ignore
                buttons: true,
            })
                .then((willProceed) => {
                    if (willProceed) {
                        submitOrderRequest();
                    } else {
                        swal(t("alerts.order.cancelledOrderTitle"), t("alerts.order.hasBeenCancelled"), "error");
                    }
                });
        } else {
            swal({
                title: t("alerts.email.unverifiedEmailFound"),
                text: t("alerts.email.unverifiedEmailFoundMessage", {unverifiedEmail: unverifiedEmail, email: email}),
                icon: "warning",
                // @ts-ignore
                buttons: true,
                dangerMode: true,
            }).then((willProceed) => {
                if (willProceed) {
                    submitOrderRequest();
                } else {
                    swal(t("alerts.order.cancelledOrderTitle"), t("alerts.order.hasBeenCancelled"), "error");
                }
            });
        }
    };

    const submitOrderRequest = () => {
        swal({
            className: "", content: undefined, dangerMode: false,
            title: t("load.processing"),
            text: t("load.loadingMessage4"),
            icon: "info",
            // @ts-ignore
            buttons: false,
            closeOnClickOutside: false,
            closeOnEsc: false,
            timer: 5000
        });

        const orderDescription = (document.getElementById("orderDescription") as HTMLInputElement).value;
        const customerFullName = (document.getElementById("customerFullName") as HTMLInputElement).value;
        const customerPhone = (document.getElementById("customerPhone") as HTMLInputElement).value;
        const customerAddress = (document.getElementById("customerAddress") as HTMLInputElement).value;
        const customerPostalCode = (document.getElementById("customerPostalCode") as HTMLInputElement).value;
        const customerCity = (document.getElementById("customerCity") as HTMLInputElement).value;
        const customerApartment = (document.getElementById("customerApartment") as HTMLInputElement).value;
        const customerDueDate = (document.getElementById("customerDueDate") as HTMLInputElement).value;

        if (!selectedServiceId || !orderDescription.trim() || !customerFullName.trim()
            || !customerAddress.trim() || !customerPostalCode.trim() || !customerCity.trim()
            || !customerDueDate.trim()
        ) {
            swal(t("alerts.order.missingInformation"), t("alerts.order.missingInformationMessage"), "warning");
            return;
        }

        let data = {
            "serviceId": selectedServiceId,
            "orderDescription": orderDescription,
            "customerFullName": customerFullName,
            "customerPhone": customerPhone,
            "customerAddress": customerAddress,
            "customerPostalCode": customerPostalCode,
            "customerCity": customerCity,
            "customerApartment": customerApartment,
            "dueDate": customerDueDate,
        };


        axios.post(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/request`, data, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 201) {
                    swal(t("alerts.success"), t("alerts.order.orderCreatedMessage"), "success").then(() => {
                        window.location.href = "/";
                    });
                }
            })
            .catch(error => {
                console.log(error);
                swal("Error", "Something went wrong", "error");
            });
    };

    const backButtonHref = "/";

    return (
        <>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />
            <div className={"create-order-customer-container"}>
                <div className="container text-center mt-4 mb-4">
                    <div className="row">
                        <div className="col-12 d-flex justify-content-center">
                            <h1>{t("customerInfo.requestOrderTitle")}</h1>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-lg-6 d-none d-lg-flex align-items-stretch justify-content-center">
                            <div className="image-container">
                                {/* eslint-disable-next-line jsx-a11y/img-redundant-alt */}
                                <img
                                    src="https://res.cloudinary.com/dszhbawv7/image/upload/v1701099346/test-image-carousel-1_xd1nwu.jpg"
                                    alt="Service Image" className="img-fluid stretch-image"/>
                            </div>
                        </div>

                        <div className="col-md-6">
                            <div className="order-creation-form text-start p-lg-2">
                                <form onSubmit={requestOrder}>

                                    <div className="row mb-3">
                                        <label htmlFor="serviceId" className="col-sm-3 col-form-label">
                                            {t("customerInfo.serviceType")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <select
                                                id="serviceId"
                                                className="form-control service-dropdown"
                                                value={selectedServiceId}
                                                onChange={(e) => {
                                                    setSelectedServiceId(e.target.value);
                                                    // @ts-ignore
                                                    const selectedService = allServices.find(service => service.serviceId === e.target.value);
                                                    // @ts-ignore
                                                    console.log('Selected service:', selectedService ? selectedService.serviceName : 'No service selected');
                                                }}
                                                required
                                            >
                                                <option value="">{t("customerInfo.serviceType")}</option>
                                                {allServices.map((service) => (
                                                    // @ts-ignore
                                                    <option key={service.serviceId} value={service.serviceId}>
                                                        { // @ts-ignore
                                                            getValuesFromJSON(i18n.language, service.serviceName)}
                                                    </option>
                                                ))}
                                            </select>


                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"orderDescription"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.orderDescription")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <textarea
                                                required
                                                name={"orderDescription"}
                                                id={"orderDescription"}
                                                className="form-control"
                                                placeholder={t("customerInfo.orderDescription")}
                                                minLength={10}
                                                maxLength={500}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerFullName"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.fullName")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerFullName"}
                                                id={"customerFullName"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("customerInfo.fullName")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerPhone"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.phoneNumber")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                name={"customerPhone"}
                                                id={"customerPhone"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("customerInfo.phoneNumber")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerAddress"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.address")}*
                                        </label>
                                        {/*// @ts-ignore*/}
                                        <div className="col-sm-9">
                                            <input onKeyDown={(e) => {
                                                e.key === 'Enter' && e.preventDefault()

                                                // @ts-ignore
                                            }} ref={ref} type="text"
                                                   placeholder={t("customerInfo.address")}
                                                   required={true}
                                                   name={"customerAddress"}
                                                   id={"customerAddress"}
                                                   className="form-control"
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerPostalCode"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.postalCode")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerPostalCode"}
                                                id={"customerPostalCode"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("customerInfo.postalCode")}
                                            />
                                        </div>

                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerCity"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.city")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerCity"}
                                                id={"customerCity"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("customerInfo.city")}
                                            />
                                        </div>

                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerApartment"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.apartmentNumber")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                name={"customerApartment"}
                                                id={"customerApartment"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("customerInfo.apartmentNumber")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerDueDate"} className="col-sm-3 col-form-label">
                                            {t("customerInfo.DueDate")}*
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerDueDate"}
                                                id={"customerDueDate"}
                                                min={minDate}
                                                type="date"
                                                className="form-control"
                                                placeholder={t("customerInfo.DueDate")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col-sm-9 offset-sm-3 d-flex justify-content-end">
                                            <button
                                                type="button"
                                                className="btn btn-style"
                                                onClick={requestOrder}
                                            >
                                                {t("submitButton")}
                                            </button>
                                        </div>
                                    </div>


                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default RequestOrder;
