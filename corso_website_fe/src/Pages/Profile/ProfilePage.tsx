import axios from "axios";
import {useAuth} from "../../security/Components/AuthProvider";
import Cookies from "js-cookie";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import "./ModifyProfile.css";
import React, {useEffect, useState} from "react";
import swal from "sweetalert";
import {usePlacesWidget} from "react-google-autocomplete"; // Import the styles from ModifyProfile.css



function ProfilePage() {
    const auth = useAuth();

    const [hasProfile, setHasProfile] = useState(false);


    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyATOXwze5SV7A2-XyRvwT9WIPJxfA2bSPk",
        onPlaceSelected: (place) => {
            try {
            document.getElementById("city")!.setAttribute("value", place.address_components[2].long_name)
            document.getElementById("postalCode")!.setAttribute("value", place.address_components[place.address_components.length - 1].long_name)
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


    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated) {
            window.location.href = "http://localhost:3000/";
        }


        checkIfProfileExists().then(r => {
            console.log(r)
        })


        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    const checkIfProfileExists = async () => {
        await axios.get("http://localhost:8080/api/v1/corso/customers?simpleCheck=true", {
            headers: {
                // @ts-ignore
                "X-XSRF-TOKEN": auth.getXsrfToken(),
            }
        }).then(r => {
            console.log(r.status);
            if (r.status === 200) {
                setHasProfile(true);
                console.log(hasProfile);
                axios.get("http://localhost:8080/api/v1/corso/customers", {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                }).then(r => {
                    if (r.status === 200) {
                        console.log(r.data);
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
                        console.log(e);
                    }
                )
            } else {
                setHasProfile(false);
                document.getElementById("name")!.setAttribute("value", Cookies.get("username")!);
                document.getElementById("email")!.setAttribute("value", Cookies.get("email")!);
            }
        }).catch(e => {
                console.log(e);
                setHasProfile(false);
                document.getElementById("name")!.setAttribute("value", Cookies.get("username")!);
                document.getElementById("email")!.setAttribute("value", Cookies.get("email")!);
            }
        )


    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const phoneInput = document.getElementById("phone") as HTMLInputElement;
        const phoneValue = phoneInput.value;
        const phonePattern = /^\+?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/im;
        if (phoneValue !== "" && !phonePattern.test(phoneValue)) {
            swal("Invalid Phone Number", "Please enter a valid phone number\n\nValid formats:\n" +
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
            swal("Invalid Postal Code", "Please enter a valid Canadian postal code (e.g., A1A 1A1)", "error");
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
            axios.put("http://localhost:8080/api/v1/corso/customers", body, {
                headers: {
                    // @ts-ignore
                    "X-XSRF-TOKEN": auth.getXsrfToken(),
                },
            }).then(r => {
                if (r.status === 200) {
                    console.log(r.data);
                    Cookies.set("username", r.data.name);
                    Cookies.set("email", r.data.email);
                    swal("Profile Updated Successfully", "", "success")
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


                    swal("Error!", "A or multiple errors present !\n" + errors, "error").then(r => {
                        // @ts-ignore
                        auth.authError(e.response.status)
                    })
                });
        } else {
            axios
                .post("http://localhost:8080/api/v1/corso/customers", body, {
                    headers: {
                        // @ts-ignore
                        "X-XSRF-TOKEN": auth.getXsrfToken(),
                    },
                })
                .then((r: any) => {
                    if (r.status === 201) {
                        console.log(r.data);
                        Cookies.set("username", r.data.name);
                        Cookies.set("email", r.data.email);
                        swal("Profile Created Successfully", "", "success")
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


                swal("Error!", "A or multiple errors present !\n" + errors, "error").then(r => {
                    // @ts-ignore
                    auth.authError(e.response.status)
                })
            })
        }
    };


    const deleteProfile = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            const response = await axios.delete("http://localhost:8080/api/v1/corso/customers", {
                headers: {
                    // @ts-ignore
                    "X-XSRF-TOKEN": auth.getXsrfToken(),
                }
            });

            console.log(response);
            if (response.status === 204) {
                console.log(response.data);
                Cookies.remove("username");
                Cookies.remove("email");

                await swal("Profile Deleted Successfully", "Only your default profile was kept (username and email)!", "success");

                axios.get("http://localhost:8080/api/v1/corso/security/user-info", {

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
                        console.log(e);
                        if (e.response.status === 401 || e.response.status === 403) {
                            // @ts-ignore
                            auth.authError(e.response.status);
                        }else if(e.response.status === 404){
                            swal("No Profile Found", "You don't have a profile yet!", "warning")
                        }else {
                            swal("Error!", e.response.data.message, "error")
                        }
                    }
                )
                    setHasProfile(false);
            } else {
                swal("Error!", "Something went wrong", "error");
            }
        } catch (e:any) {
            if (e.response.status === 401 || e.response.status === 403) {
                // @ts-ignore
                auth.authError(e.response.status);
            }else if(e.response.status === 404){
                swal("No Profile Found", "You don't have any information saved !", "warning")
            }else {
                swal("Error!", e.response.data.message, "error")
            }
        }
    };

    return (
        <div>
            <NavigationBar/>
            <div className="profile-page">
                <div className="profile-box mt-5 mb-5">
                    <h1>Profile</h1>

                    <p className={"text-center fs-4 m-3"}>
                        Please enter your information below. This information will be used to fill in the order form
                        automatically. This information will be saved and can be modified at any time. If your email is invalid, we will use the email you used to signup.
                    </p>

                    <form onSubmit={handleSubmit} className="container">
                        <div className="row w-75 m-auto">
                            <label>
                                Name:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="username" className="form-control mb-2" placeholder="Name"
                                       id={"name"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                Email:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="email" className="form-control mb-2" placeholder="Email"
                                       id={"email"}
                                       required={true}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                Address:
                                {/*// @ts-ignore*/}
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} ref={ref} type="text" name="address" className="form-control mb-2" placeholder="Address"
                                       id={"address"}
                                       required={true}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                Postal Code:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="postalCode" className="form-control mb-2"
                                       placeholder="Postal Code" required={true}
                                       id={"postalCode"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                City:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="city" className="form-control mb-2" placeholder="City"
                                       required={true} id={"city"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                Apartment Number:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="apartmentNumber" className="form-control mb-2"
                                       placeholder="Apartment Number" id={"apartmentNumber"}/>
                            </label>
                        </div>
                        <div className="row w-75 m-auto">
                            <label>
                                Phone:
                                <input    onKeyDown={(e) => { e.key === 'Enter' && e.preventDefault() }} type="text" name="phone" className="form-control mb-2" placeholder="Phone"
                                       id={"phone"}/>
                            </label>
                        </div>
                        <div className="row w-25 m-auto"
                             style={{margin: "auto", width: "50%", marginTop: "20px", marginBottom: "120px"}}>
                            <button id={"save-btn"}
                                type="submit"
                                className="btn btn-primary"
                                style={{
                                    margin: "auto",
                                    borderRadius: "50px",
                                    background: " #064BEC",
                                    color: "#FFF",
                                    fontFamily: "Nunito",
                                    fontSize: "20px",
                                    fontWeight: "700",
                                    marginBottom: "20px",
                                }}
                            >
                                Submit
                            </button>
                        </div>
                    </form>

                    <form onSubmit={deleteProfile} className="container">
                        <div className="row w-50 m-auto"
                             style={{margin: "auto", marginTop: "20px", marginBottom: "120px"}}>
                            <button
                                type="submit"
                                className="btn btn-primary cancel-btn"
                            >
                                Delete Information
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;
