import React, {useEffect} from "react";
import {useAuth} from "../../security/Components/AuthProvider";
import axios from "axios";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import "./CreateExternalOrder.css"
import Dropdown from "../../Components/Dropdown/droptown";
import swal from "sweetalert";
import searchIcon from "../../images/Search_light.svg"
import {usePlacesWidget} from "react-google-autocomplete";

function CreateExternalOrderPage() {

    const [users, setUsers] = React.useState([])

    const [selectedUser, setSelectedUser] = React.useState("")

    const [selectedRowIndex, setSelectedRowIndex] = React.useState(-1);


    const [allServices, setAllServices] = React.useState([])

    const [selectedSearchField, setSelectedSearchField] = React.useState("name")

    const auth = useAuth()


    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyATOXwze5SV7A2-XyRvwT9WIPJxfA2bSPk",
        onPlaceSelected: (place) => {
            try {
                document.getElementById("customerAddress")!.setAttribute("value", place.formatted_address)
                document.getElementById("customerCity")!.setAttribute("value", place.address_components[2].long_name)
                document.getElementById("customerPostalCode")!.setAttribute("value", place.address_components[place.address_components.length - 1].long_name)
                console.log(place)
            }catch (e) {
                console.log(e)
            }
        },
        options: {
            types: ["address"],
            componentRestrictions: { country: "ca" },
        }
    })

    const getUsers = async (event: React.FormEvent<HTMLFormElement>) => {

        event.preventDefault()

        if ((document.getElementById("search-value") as HTMLInputElement).value.replaceAll(/\s/g, '').length === 0) {
            return
        }
        let param = "?" + selectedSearchField + "=" + (document.getElementById("search-value") as HTMLInputElement).value

        axios.get("http://localhost:8080/api/v1/corso/auth0/manage/users" + param, {
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

        axios.get("http://localhost:8080/api/v1/corso/services", {
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


        console.log((document.getElementById("serviceId") as HTMLInputElement).value)

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
            "daysLeft": (document.getElementById("daysLeft") as HTMLInputElement).value,
            "progressInformation": (document.getElementById("progressText") as HTMLInputElement).value,
        }

        axios.post("http://localhost:8080/api/v1/corso/orders/external", data, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 201) {
                    swal("Success!", "Order created successfully!", "success")
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

                swal("Error!", "An error occurred while creating the order! \n " + errors, "error").then(() => {
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

    // @ts-ignore
    return (
        <div className={"text-center"}>
            <NavigationBar/>
            <div className={"search-banner w-100 mt-3"}>
                <h2 className={"text-white"}>Search for user</h2>
                <div className={"search-bar m-auto"}>

                    <form onSubmit={getUsers}>
                        <Dropdown
                            label={selectedSearchField.charAt(0).toUpperCase() + selectedSearchField.slice(1)}
                            options={["name", "email", "username"]}
                            onSelect={(option: any) => {
                                setSelectedSearchField(option)
                            }}
                        />
                        <input required name={"search-value"} id={"search-value"} type="text"
                               placeholder="Search for user"/>
                        <button type={"submit"}>
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
                                <th>Name</th>
                                <th>Email</th>
                                <th>Username</th>
                            </tr>
                            {
                                users.length === 0 &&
                                <tr>
                                    <td colSpan={3}>No users found</td>
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
                                            className={"user-row " && selectedRowIndex === index ? "selected-row" : "" }
                                        >
                                            <td>{user.name || "Not Applicable"}</td>
                                            <td>{user.email || "Not Applicable"}</td>
                                            <td>{user.username || "Not Applicable"}</td>
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
                    console.log(ref)
                }
            }
                    className={"mt-1 mb-5 btn-style"}
            >
                Clear User Selection
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
                                    Select the service that is the most appropriate for the order.
                                </span>
                            </div>
                            <div className="col-sm-3">

                                <select name="serviceId" id="serviceId" required>
                                    {
                                        allServices.map((service: any) => {
                                            return (
                                                <option key={service.serviceId}
                                                        value={service.serviceId}>{service.serviceName}</option>
                                            )
                                        })
                                    }
                                </select>

                            </div>
                        </div>
                        <div className="row mb-3">
                            <label htmlFor={"dueDate"} className="col-sm-3 col-form-label">
                                Due Date
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"dueDate"}
                                    id={"dueDate"}
                                    type="date"
                                    className="form-control"
                                    placeholder="Due Date"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"orderDescription"} className="col-sm-3 col-form-label">
                                Order Description
                            </label>
                            <div className="col-sm-9">
                                <textarea
                                    name={"orderDescription"}
                                    id={"orderDescription"}
                                    className="form-control"
                                    placeholder="Order Description"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerFullName"} className="col-sm-3 col-form-label">
                                Customers Full Name
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerFullName"}
                                    id={"customerFullName"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Customer Full Name"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerEmail"} className="col-sm-3 col-form-label">
                                Customer Email
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerEmail"}
                                    id={"customerEmail"}
                                    type="email"
                                    className="form-control"
                                    placeholder="Customer Email"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerPhone"} className="col-sm-3 col-form-label">
                                Customer Phone
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerPhone"}
                                    id={"customerPhone"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Customer Phone"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerAddress"} className="col-sm-3 col-form-label">
                                Customer Address
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
                                    placeholder="Customer Address"
                                >

                                </input>
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerPostalCode"} className="col-sm-3 col-form-label">
                                Customer Postal Code
                            </label>
                            <div className="col-sm-9">
                                <input
                                    required
                                    name={"customerPostalCode"}
                                    id={"customerPostalCode"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Customer Postal Code"
                                />
                            </div>

                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerCity"} className="col-sm-3 col-form-label">
                                Customer City
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerCity"}
                                    id={"customerCity"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Customer City"
                                />
                            </div>

                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"customerApartment"} className="col-sm-3 col-form-label">
                                Customer Apartment Number
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"customerApartment"}
                                    id={"customerApartment"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Customer Apartment Number"
                                />
                            </div>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"estimatedDuration"} className="col-sm-3 col-form-label">
                                Estimated Duration
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"estimatedDuration"}
                                    id={"estimatedDuration"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Estimated Duration"
                                />
                            </div>
                        </div>

                        <div className="row mb-3 text-start">
                        <span className="col-sm-9 offset-sm-3 fs-6">
                            If the job was started before this form was filled, enter how many days are left, otherwise leave blank.
                        </span>
                        </div>

                        <div className="row mb-3">
                            <label htmlFor={"daysLeft"} className="col-sm-3 col-form-label">
                                Days Left
                            </label>
                            <div className="col-sm-9">
                                <input
                                    name={"daysLeft"}
                                    id={"daysLeft"}
                                    type="text"
                                    className="form-control"
                                    placeholder="Days Left"
                                />
                            </div>
                        </div>

                            <div className="row mb-3 text-start">
                                <label htmlFor={"daysLeft"} className="col-sm-3 col-form-label">
                                    Progress Description
                                </label>
                                <div className="col-sm-9">
                                    <textarea
                                        name={"progressText"}
                                        id={"progressText"}
                                        className="form-control"
                                        placeholder="Describe what was done"
                                    />
                                </div>
                            </div>

                        <div className="row mb-3">
                            <div className="col-sm-9 offset-sm-3">
                                <button id={"submit-order"} type={"submit"} className="btn-style">
                                    Create Order
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