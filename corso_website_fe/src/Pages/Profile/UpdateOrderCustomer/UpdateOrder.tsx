import React, {useEffect, useState} from 'react';
import {useAuth} from "../../../security/Components/AuthProvider";
import axios from "axios";
import Order from "../../../ressources/Models/Order";
import ServiceOrder from "../../../ressources/Models/ServiceOrder";
import {useParams} from "react-router-dom";
import {usePlacesWidget} from "react-google-autocomplete";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import swal from "sweetalert";
import '../../globalStyling/globalStyling.css';
import './UpdateOrder.css'
import {SyncLoader} from "react-spinners";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";

function UpdateOrder() {

    const auth = useAuth();
    const {t} = useTranslation();


    const [isLoading, setIsLoading] = useState<boolean>(false);


    const {ref} = usePlacesWidget({
        apiKey: process.env.REACT_APP_MAPS_API_KEY,
        onPlaceSelected: (place) => {
            try {
                document.getElementById("customerAddress")!.setAttribute("value", place.formatted_address)
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

    let {orderId} = useParams();

    const [order, setOrder] = useState<Order>(new Order("", "", "", new ServiceOrder("", "", ""), "", "", "", "", "", "", "", "", "", "", "", "", "", 1, 1));

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes('Customer')) {
            window.location.href = '/';
            return
        }


        getOrder()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const getOrder = () => {
        axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/${orderId}`,
            {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            }
        )
            .then(res => {
                const order = res.data;

                setOrder(order);

            })
            .catch(err => {
                

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }

            })

    }


    const updateOrder = async (event: any) => {

        event.preventDefault();

        const phoneInput = document.getElementById("customerPhone") as HTMLInputElement;
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

        setIsLoading(true)
        axios.patch(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders/${orderId}`,
            {
                customerPhone: event.target.customerPhone.value,
                customerAddress: event.target.customerAddress.value,
                customerPostalCode: event.target.customerPostalCode.value,
                customerCity: event.target.customerCity.value,
                customerApartmentNumber: event.target.customerApartment.value
            }
            , {
                headers: {
                    // @ts-ignore
                    'X-XSRF-TOKEN': auth.getXsrfToken()
                }
            })
            .then(res => {
                const order = res.data;
                if (res.status === 200) {
                    setOrder(order);

                    swal(t("alerts.order.orderUpdatedTitle"), t("alerts.order.orderUpdatedMessage"), "success")
                        .then(() => {
                            window.location.replace(document.referrer);
                        })
                }

            })
            .catch(err => {
                

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status);
                }
            }).then(() => {
            setIsLoading(false)
        })
    }

    const backButtonHref = "/orders/" + order.orderId;

    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("back")}
            />
            <div className={"update-order-customer-box px-5"}>
                <h1>{t("order.updateOrder")}</h1>
                <h2>{t("order.orderId")}: #{order.orderTrackingNumber}</h2>
                <form onSubmit={updateOrder} className={"w-100"}>
                    <div className="row mb-3">
                        <label htmlFor={"customerAddress"} className="col-sm-2 col-form-label">
                            {t("customerInfo.address")}
                        </label>
                        <div className="col-sm-10">
                            <input
                                // @ts-ignore
                                ref={ref}
                                required
                                name={"customerAddress"}
                                id={"customerAddress"}
                                type="text"
                                className="form-control"
                                placeholder="Customer Address"
                                defaultValue={order.customerAddress}
                            >

                            </input>
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor={"customerApartment"} className="col-sm-3 col-form-label">
                            {t("customerInfo.apartmentNumber")}
                        </label>
                        <div className="col-sm-4">
                            <input
                                name={"customerApartment"}
                                id={"customerApartment"}
                                type="text"
                                className="form-control"
                                placeholder={t("customerInfo.apartmentNumber")}
                                defaultValue={order.customerApartmentNumber}
                            />
                        </div>

                        <label htmlFor={"customerPostalCode"} className="col-sm-2 col-form-label">
                            {t("customerInfo.postalCode")}
                        </label>
                        <div className="col-sm-3">
                            <input
                                required
                                name={"customerPostalCode"}
                                id={"customerPostalCode"}
                                type="text"
                                className="form-control"
                                placeholder={t("customerInfo.postalCodeCustomer")}
                                defaultValue={order.customerPostalCode}
                            />
                        </div>
                    </div>

                    <div className="row">
                        <label htmlFor={"customerPhone"} className="col-sm-2 col-form-label">
                            {t("customerInfo.phoneNumber")}
                        </label>
                        <div className="col-sm-5">
                            <input
                                name={"customerPhone"}
                                id={"customerPhone"}
                                type="text"
                                className="form-control"
                                placeholder={t("customerInfo.phoneNumberCustomer")}
                                defaultValue={order.customerPhone}
                            />
                        </div>

                        <label htmlFor={"customerCity"} className="col-sm-2 col-form-label">
                            {t("customerInfo.city")}
                        </label>
                        <div className="col-sm-3">
                            <input
                                name={"customerCity"}
                                id={"customerCity"}
                                type="text"
                                className="form-control"
                                placeholder={t("customerInfo.cityCustomer")}
                                defaultValue={order.customerCity}
                            />
                        </div>

                    </div>


                    <div className="m-3">
                        <div className={isLoading ? "d-block spinner-visible" : "d-none"} style={{
                            padding: "10px 30px",

                        }}>
                            <SyncLoader className={"spinner"} color="#054AEB"/>
                        </div>


                        <button type="submit" className={ isLoading ? "d-none btn-style" : "btn-style"} style={{
                            padding: "10px 30px",
                        }}>
                            {t("order.updateOrder")}
                        </button>
                    </div>
                </form>
            </div>

        </div>
    );
}

export default UpdateOrder;
