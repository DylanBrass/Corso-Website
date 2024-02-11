import React, {useEffect, useState} from 'react';
import {useAuth} from "../../../security/Components/AuthProvider";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import axios from "axios";
import swal from "sweetalert";
import Cookies from "js-cookie";
import {usePlacesWidget} from "react-google-autocomplete";
import {useTranslation} from "react-i18next";
import './RequestOrderCustomer.css';

function RequestOrder() {
    const {t} = useTranslation("translation");

    const auth = useAuth();
    const [allServices, setAllServices] = useState([]);
    const [selectedServiceId, setSelectedServiceId] = useState('');

    const [hasProfile, setHasProfile] = useState(false);
    const [isVerified, setIsVerified] = useState(false);

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

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Customer')) {
            window.location.href = '/';
            return
        }

        checkIfProfileExists().then(r => {

        })

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
                setHasProfile(false);
                document.getElementById("customerFullName")!.setAttribute("value", Cookies.get("username")!);
            }
        }).catch(e => {

                setHasProfile(false);
                setIsVerified(true)
                document.getElementById("customerFullName")!.setAttribute("value", Cookies.get("username")!);
            }
        )

    }

    const {ref} = usePlacesWidget({
        apiKey: "AIzaSyATOXwze5SV7A2-XyRvwT9WIPJxfA2bSPk",
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
        // Show a loading message
        swal({
            className: "", content: undefined, dangerMode: false,
            title: "Processing...",
            text: "Please wait while your request is being processed.",
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

        if (!selectedServiceId || !orderDescription.trim() || !customerFullName.trim() || !customerPhone.trim() || !customerAddress.trim() || !customerPostalCode.trim() || !customerCity.trim()) {
            swal("Missing Information", "Please fill in all required fields before submitting your order.", "warning");
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
        };


        axios.post(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/request`, data, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(response => {
                if (response.status === 201) {
                    swal("Success!", "Order created successfully!", "success").then(() => {
                        window.location.href = "/";
                    });
                }
            })
            .catch(error => {
                console.log(error);
                swal("Error", "Something went wrong", "error");
            });
    };

    return (
        <>
            <NavigationBar/>
            <div className={"create-order-customer-container"}>
                <div className="container text-center mt-4 mb-4">
                    <div className="row">
                        <div className="col-12 d-flex justify-content-center">
                            <h1>{t("requestOrderTitle")}</h1>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-lg-6 d-none d-lg-flex align-items-stretch justify-content-center">
                            <div className="image-container">
                                <img src="https://res.cloudinary.com/dszhbawv7/image/upload/v1701099346/test-image-carousel-1_xd1nwu.jpg" alt="Service Image" className="img-fluid stretch-image" />
                            </div>
                        </div>

                        <div className="col-md-6">
                            <div className="order-creation-form text-start p-lg-2">
                                <form onSubmit={requestOrder}>

                                    <div className="row mb-3">
                                        <label htmlFor="serviceId" className="col-sm-3 col-form-label">
                                            {t("serviceType")}
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
                                                <option value="">{t("serviceType")}</option>
                                                {allServices.map((service) => (
                                                    // @ts-ignore
                                                    <option key={service.serviceId} value={service.serviceId}>
                                                        {// @ts-ignore
                                                            service.serviceName}
                                                    </option>
                                                ))}
                                            </select>


                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"orderDescription"} className="col-sm-3 col-form-label">
                                            {t("orderDescription")}
                                        </label>
                                        <div className="col-sm-9">
                                            <textarea
                                                name={"orderDescription"}
                                                id={"orderDescription"}
                                                className="form-control"
                                                placeholder={t("orderDescription")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerFullName"} className="col-sm-3 col-form-label">
                                            {t("fullName")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerFullName"}
                                                id={"customerFullName"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("fullName")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerPhone"} className="col-sm-3 col-form-label">
                                            {t("phoneNumber")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                name={"customerPhone"}
                                                id={"customerPhone"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("phoneNumber")}
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerAddress"} className="col-sm-3 col-form-label">
                                            {t("address")}
                                        </label>
                                        {/*// @ts-ignore*/}
                                        <div className="col-sm-9">
                                            <input onKeyDown={(e) => {
                                                e.key === 'Enter' && e.preventDefault()

                                                // @ts-ignore
                                            }} ref={ref} type="text"
                                                   placeholder={t("address")}
                                                   required={true}
                                                   name={"customerAddress"}
                                                   id={"customerAddress"}
                                                   className="form-control"
                                            />
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerPostalCode"} className="col-sm-3 col-form-label">
                                            {t("postalCode")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerPostalCode"}
                                                id={"customerPostalCode"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("postalCode")}
                                            />
                                        </div>

                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerCity"} className="col-sm-3 col-form-label">
                                            {t("city")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                required
                                                name={"customerCity"}
                                                id={"customerCity"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("city")}
                                            />
                                        </div>

                                    </div>

                                    <div className="row mb-3">
                                        <label htmlFor={"customerApartment"} className="col-sm-3 col-form-label">
                                            {t("apartmentNumber")}
                                        </label>
                                        <div className="col-sm-9">
                                            <input
                                                name={"customerApartment"}
                                                id={"customerApartment"}
                                                type="text"
                                                className="form-control"
                                                placeholder={t("apartmentNumber")}
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
