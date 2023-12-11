import NavigationBar from "../Components/NavigationBar";
import {useAuth} from "../security/Components/AuthProvider";
import axios from "axios";
import {useEffect} from "react";
import "./CreateAdmin.css"
function CreateAdminPage() {
    const auth = useAuth();

    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "http://localhost:3000/";
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const addAdmin = async (event: any) => {
        event.preventDefault();
        // @ts-ignore
        await axios.post("http://localhost:8080/api/v1/corso/auth0/manage/add-admin", {
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
                    alert("Admin Created Successfully")
                    window.location.href = "http://localhost:3000/dashboard";

                }
            })
            .catch(e => {
                alert(e.response.data.message)
                // @ts-ignore
                auth.authError(e.response.status)
            })
    }
    return (
        <div>
            <NavigationBar/>
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
                                <h5>Create Admin Account</h5>
                            </div>
                            <div className={"row form-admin-box w-75 m-auto"}>
                                <form onSubmit={addAdmin} style={{

                                }}>
                                    <div className="mt-4">
                                        <div className="row">
                                                <label htmlFor="username" className="form-label">Username</label>
                                                <input className="form-control mb-2" type="text"
                                                       placeholder="Username"/>
                                        </div>
                                        <div className="row">
                                            <label htmlFor="email" className="form-label">Email</label>
                                                <input className="form-control mb-2" type="text" placeholder="Email"/>
                                        </div>
                                        <div className="row">
                                            <label htmlFor="password" className="form-label">Password</label>
                                                <input className="form-control mb-2" type="password"
                                                       placeholder="Password"/>
                                        </div>
                                        <div className="row" style={{
                                            margin: "auto",
                                            width: "50%",
                                            marginTop: "20px",
                                            marginBottom: "120px"
                                        }}>
                                                <button className="btn btn-primary" type="submit" style={{
                                                    borderRadius: "50px",
                                                    background:" #064BEC",
                                                    color: "#FFF",
                                                    fontFamily: "Nunito",
                                                    fontSize: "20px",
                                                    fontWeight: "700"
                                                }}>Add Admin</button>
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