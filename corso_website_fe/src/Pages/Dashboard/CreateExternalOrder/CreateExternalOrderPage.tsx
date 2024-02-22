import React, {useEffect, useState} from "react";
import {useAuth} from "../../../security/Components/AuthProvider";
import axios from "axios";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import "./CreateExternalOrder.css"
import Dropdown from "../../../Components/Dropdown/droptown";
import swal from "sweetalert";
import searchIcon from "../../../ressources/images/Search_light.svg"
import {usePlacesWidget} from "react-google-autocomplete";
import {useTranslation} from "react-i18next";
import {getValuesFromJSON} from "../../../Services/TranslationTools";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';


function CreateExternalOrderPage() {

    const {t, i18n} = useTranslation();

    const isFrench = i18n.language === 'fr';

    const [users, setUsers] = React.useState([])

    const [selectedUser, setSelectedUser] = React.useState("")

    const [selectedRowIndex, setSelectedRowIndex] = React.useState(-1);


    const [allServices, setAllServices] = React.useState([])

    let [selectedSearchField, setSelectedSearchField] = useState(t("userOptions.email"));

    const auth = useAuth()

    useEffect(() => {
        setSelectedSearchField(t("userOptions.email"));
    }, [i18n.language, t]);

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

    const getUsers = async (event: React.FormEvent<HTMLFormElement>) => {

        event.preventDefault()

        if ((document.getElementById("search-value") as HTMLInputElement).value.replaceAll(/\s/g, '').length < 3) {
            swal((t("alerts.enter3CharMinWarning")), "", "warning")
            return
        }

        if (isFrench) {
            switch (selectedSearchField) {
                case "courriel":
                    selectedSearchField = "email";
                    break;
                case "nom":
                    selectedSearchField = "name";
                    break;
                case "nom d'utilisateur":
                    selectedSearchField = "username";
                    break;
                default:
                    selectedSearchField = "email";
                    break;
            }
        }

        let param = "?" + selectedSearchField + "=" + (document.getElementById("search-value") as HTMLInputElement).value

        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/auth0/manage/users" + param, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    setUsers(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })

    }

    const getServices = async () => {

        axios.get(process.env.REACT_APP_BE_HOST + "api/v1/corso/services", {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    setAllServices(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }


    const createOrder = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault()


        let data = {
            "serviceId": (document.getElementById("serviceId") as HTMLInputElement).value,
            "userId": selectedUser,
            "dueDate": (document.getElementById("dueDate") as HTMLInputElement).value,
            "orderDescription": (document.getElementById("orderDescription") as HTMLInputElement).value,
            "customerFullName": (document.getElementById("customerFullName") as HTMLInputElement).value,
            "customerEmail": (document.getElementById("customerEmail") as HTMLInputElement).value,
            "customerPhone": (document.getElementById("customerPhone") as HTMLInputElement).value,
            "customerAddress": (document.getElementById("customerAddress") as HTMLInputElement).value,
            "customerPostalCode": (document.getElementById("customerPostalCode") as HTMLInputElement).value,
            "customerCity": (document.getElementById("customerCity") as HTMLInputElement).value,
            "customerApartment": (document.getElementById("customerApartment") as HTMLInputElement).value,
            "estimatedDuration": (document.getElementById("estimatedDuration") as HTMLInputElement).value,
            "hoursWorked": (document.getElementById("hoursWorked") as HTMLInputElement).value,
            "progressInformation": (document.getElementById("progressText") as HTMLInputElement).value,
        }

        if (parseInt(data.hoursWorked) > parseInt(data.estimatedDuration)) {
            swal((t("alerts.error")),(t("alerts.hoursWorkedConstraint")), "error")
            return
        }

        axios.post(process.env.REACT_APP_BE_HOST + "api/v1/corso/orders/external", data, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 201) {
                    swal((t("alerts.success")), (t("alerts.order.orderCreatedMessage")), "success")
                        .then(() => {
                            window.location.href = "/dashboard"
                        })
                }
            })
            .catch(e => {
                let errors = ""
                if (e.response.data.errors !== undefined) {
                    // @ts-ignore
                    e.response.data.errors.forEach((error: any) => {
                        errors += error.defaultMessage + "\n";
                    })
                }

                swal((t("alerts.error")), (t("alerts.order.orderCreationFailedMessage")) + errors, "error").then(() => {
                    // @ts-ignore
                    auth.authError(e.response.status)
                })
            })
    }


    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "/"
        } else {
            getServices()
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const backButtonHref = "/dashboard";

    // @ts-ignore
    return (
        <div className={"text-center"}>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div className={"search-banner w-100 mt-3"}>
                <h2 className={"text-white"}>{t("userOptions.searchForUser")}</h2>
                <div className={"search-bar m-auto"}>

                    <form onSubmit={getUsers}>
                        <Dropdown
                            label={selectedSearchField.charAt(0).toUpperCase() + selectedSearchField.slice(1)}
                            options={[
                                t("userOptions.email"),
                                t("userOptions.name"),
                                t("userOptions.username"),
                            ]}
                            onSelect={(option: any) => {
                                setSelectedSearchField(option)
                            }}
                        />
                        <input required name={"search-value"} id={"search-value"} type="text"
                               placeholder={t("userOptions.searchForUser")}/>
                        <button type={"submit"} className={"btn-style"}>
                            <img src={searchIcon} alt={"Search"}/>
                        </button>

                    </form>
                </div>

            </div>
            <div className={"container w-100 text-center mb-3 mt-5"}>


                <div className={"user-picker w-100 text-center m-auto"}>
                    <div className={"users-list user-list-box table-container"}>
                        <table className={"w-100"}>
                            <tbody className={"users-list user-list-box"}>
                            <tr>
                                <th>{t("userOptions.name")}</th>
                                <th>{t("userOptions.email")}</th>
                                <th>{t("userOptions.username")}</th>
                            </tr>
                            {
                                users.length === 0 &&
                                <tr>
                                    <td colSpan={3}>{t("userOptions.noUsersFound")}</td>
                                </tr>
                            }

                            {users.length !== 0 &&

                                users.map((user: any, index: number) => {
                                    return (
                                        <tr
                                            key={user.user_id}
                                            onClick={() => {
                                                document.getElementById("customerAddress")!.innerText = user.address || "";
                                                setSelectedUser(user.user_id);
                                                document.getElementById("customerFullName")!.setAttribute("value", user.name || "");
                                                document.getElementById("customerEmail")!.setAttribute("value", user.email || "");
                                                document.getElementById("customerPhone")!.setAttribute("value", user.phone || "");
                                                document.getElementById("customerAddress")!.setAttribute("value", user.address || "");
                                                document.getElementById("customerPostalCode")!.setAttribute("value", user.postalCode || "");
                                                document.getElementById("customerCity")!.setAttribute("value", user.city || "");
                                                document.getElementById("customerApartment")!.setAttribute("value", user.apartment || "");
                                                setSelectedRowIndex(index);
                                            }
                                            }
                                            className={"user-row " && selectedRowIndex === index ? "selected-row" : ""}
                                        >
                                            <td>{user.name ||  (t("userOptions.notApplicable"))}</td>
                                            <td>{user.email ||  (t("userOptions.notApplicable"))}</td>
                                            <td>{user.username ||  (t("userOptions.notApplicable"))}</td>
                                        </tr>
                                    )
                                })
                            }
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
            <button onClick={
                () => {
                    setUsers([])
                    setSelectedUser("")
                    setSelectedRowIndex(-1)
                    document.getElementById("customerFullName")!.setAttribute("value", "");
                    document.getElementById("customerEmail")!.setAttribute("value", "");
                    document.getElementById("customerPhone")!.setAttribute("value", "");
                    document.getElementById("customerAddress")!.setAttribute("value", "");
                    document.getElementById("customerPostalCode")!.setAttribute("value", "");
                    document.getElementById("customerCity")!.setAttribute("value", "");
                    document.getElementById("customerApartment")!.setAttribute("value", "");

                }
            }
                    className={"mt-1 mb-5 btn-style"}
            >
                {t("userOptions.clearSelection")}
            </button>
            <div className={"container w-100 text-center"}>
                <div className="order-creation-form w-75 text-start text-md-end m-auto">
                    <form onSubmit={createOrder}>
                        <div className="row mb-3">
                            <label htmlFor="serviceId" className="col-sm-3 col-form-label">
                                Service
                            </label>
                            <div className="col-sm-6">
                                <span>
                                    {t("createExternalOrder.selectService")}
                                </span>
                            </div>
                            <div className="col-sm-3">

                                <select className={"w-100"} name="serviceId" id="serviceId" required>
                                    {
                                        allServices.map((service: any) => {
                                            return (
                                                <option key={service.serviceId}
                                                        value={service.serviceId}>{getValuesFromJSON(i18n.language, service.serviceName)}</option>
                                            )
                                        })
                                    }
                                </select>

                            </div>
                        </div>
                        <div className="row mb-3">
                            <label htmlFor={"dueDate"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.dueDate")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"dueDate"}
                                    id={"dueDate"}
                                    type="date"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.dueDate")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"orderDescription"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.orderDescription")}
                            </label>
                            <div className="col-sm-9">
                                <textarea
                                    name={"orderDescription"}
                                    id={"orderDescription"}
                                    className="form-control"
                                    placeholder={t("createExternalOrder.orderDescription")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerFullName"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerFullName")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerFullName"}
                                    id={"customerFullName"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerFullName")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerEmail"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerEmail")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerEmail"}
                                    id={"customerEmail"}
                                    type="email"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerEmail")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerPhone"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerPhone")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerPhone"}
                                    id={"customerPhone"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerPhone")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerAddress"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerAddress")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    // @ts-ignore
                                    ref={ref}
                                    required
                                    name={"customerAddress"}
                                    id={"customerAddress"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerAddress")}
                                >

                                </input>
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerPostalCode"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerPostalCode")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerPostalCode"}
                                    id={"customerPostalCode"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerPostalCode")}
                                />
                            </div>

                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerCity"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerCity")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerCity"}
                                    id={"customerCity"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerCity")}
                                />
                            </div>

                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerApartment"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.customerApartmentNumber")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerApartment"}
                                    id={"customerApartment"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.customerApartmentNumber")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"estimatedDuration"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.estimatedDuration")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"estimatedDuration"}
                                    id={"estimatedDuration"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.estimatedDuration")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3 text-start">
                        <span className="col-sm-9 offset-sm-3 fs-6">
                            {t("createExternalOrder.jobStartedBeforeMessage")}
                        </span>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"hoursWorked"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.hoursWorked")}
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"hoursWorked"}
                                    id={"hoursWorked"}
                                    type="text"
                                    className="form-control"
                                    placeholder={t("createExternalOrder.hoursWorked")}
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"progressText"} className="col-sm-3 col-form-label">
                                {t("createExternalOrder.progressDescription")}
                            </label>
                            <div className="col-sm-9">
                                    <textarea
                                        name={"progressText"}
                                        id={"progressText"}
                                        className="form-control"
                                        placeholder={t("createExternalOrder.progressDescription")}
                                    />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <div className="col-sm-9 offset-sm-3">
                                <button id={"submit-order"} type={"submit"} className="btn-style">
                                    {t("createExternalOrder.createOrder")}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default CreateExternalOrderPage;
