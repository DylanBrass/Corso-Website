import React, {useEffect} from "react";
import {useAuth} from "../../security/Components/AuthProvider";
import axios from "axios";
import NavigationBar from "../../Components/NavigationBar";

function CreateExternalOrderPage() {

    const [users, setUsers] = React.useState([])

    const [selectedUser, setSelectedUser] = React.useState("")

    const auth = useAuth()

    const getUsers = async (event: React.FormEvent<HTMLFormElement>) => {

        event.preventDefault()

        let param = "?" + (document.getElementById("search-fields") as HTMLInputElement).value + "=" + (document.getElementById("search-value") as HTMLInputElement).value

        axios.get("http://localhost:8080/api/v1/corso/auth0/manage/users" + param, {
            headers: {
                // @ts-ignore
                'X-XSRF-TOKEN': auth.getXsrfToken()
            }
        })
            .then(r => {
                if (r.status === 200) {
                    console.log(r.data)
                    setUsers(r.data)
                }
            })
            .catch(e => {
                // @ts-ignore
                auth.authError(e.response.status)
            })

    }


    useEffect(() => {
        // @ts-ignore
        if (!auth.isAuthenticated || !auth.userRoles().includes("Admin")) {
            window.location.href = "/"
        } else {
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    return (
        <div>
            <NavigationBar/>
            <h1>Create External Order Page</h1>

            <div className={"user-picker"}>

                <form onSubmit={getUsers}>
                    <select name="search-fields" id="search-fields">
                        <option value="email">Email</option>
                        <option value="name">Name</option>
                        <option value="username">Username</option>
                    </select>

                    <input name={"search-value"} id={"search-value"} type="text" placeholder="Search for user"/>
                    <button type={"submit"}>Search</button>
                </form>

                <div className={"users-list user-list-box"}>
                    <table className={"table table-striped"}>
                        <tbody className={"users-list user-list-box"}>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Username</th>
                        </tr>
                        {
                            users.map((user: any) => {
                                return (
                                    <tr onClick={
                                        () => {
                                            setSelectedUser(user.user_id)
                                        }
                                    }>
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

            <div className={"order-creation-form"}>

                <div>{selectedUser}</div>

            </div>


        </div>
    );
}

export default CreateExternalOrderPage;