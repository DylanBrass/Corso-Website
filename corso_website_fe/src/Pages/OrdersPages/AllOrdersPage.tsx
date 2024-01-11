import React, {useState, useEffect} from 'react';
import axios from 'axios';
import OrderTile from "../../Components/OrderTile/OrderTile";
import CustomPagination from "../../Components/Pagination";
import {Order} from "../../Components/OrderTile/OrderTile";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import Dropdown from "../../Components/Dropdown/droptown";
import swal from "sweetalert";
import searchIcon from "../../images/Search_light.svg";
import {useAuth} from "../../security/Components/AuthProvider";
import "./AllOrdersPage.css"

function AllOrdersPage() {

    const auth: {} = useAuth()

    const [orders, setOrders] = useState<Order[]>([]);

    const [page, setPage] = useState(1);

    const [pageSize, setPageSize] = useState(1);

    const [totalPages, setTotalPages] = useState(1);

    const [selectedStatus, setSelectedStatus] = useState("all");

    const [previousStatus, setPreviousStatus] = useState("all");

    const [selectedUser, setSelectedUser] = useState("");

    const [previousUser, setPreviousUser] = useState("");

    const [selectedSearchFieldUser, setSelectedSearchFieldUser] = useState("name");

    const [resetKey, setResetKey] = useState<string | null>(null);


    useEffect(() => {
        fetchOrders(page, pageSize, selectedStatus, selectedUser);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page, pageSize]);

    const [users, setUsers] = React.useState([])

    const getUsers = async (event: React.FormEvent<HTMLFormElement>) => {

        event.preventDefault()

        if ((document.getElementById("search-value") as HTMLInputElement).value.replaceAll(/\s/g, '').length < 3) {
            swal("Enter at least 3 characters!", "", "warning")
            return
        }
        let param = "?" + selectedSearchFieldUser + "=" + (document.getElementById("search-value") as HTMLInputElement).value

        axios.get(process.env.REACT_APP_BE_HOST+"api/v1/corso/auth0/manage/users" + param, {
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


    const fetchOrders = async (pageNumber: number, size: number, status: string | null, user: string) => {
        try {


            let url = process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/filter?pageSize=${size}&offset=${(pageNumber - 1) * size}`;
            if (status !== null && status.toLowerCase() !== "all") {
                status = status.replace(" ", "_");
                url += `&status=${status}`;

            }

            if (user.length > 0) {
                // @ts-ignore
                url += `&userId=${user.replace("|", "%7C")}`;
            }


            const response = await axios.get(url, {
                    headers: {
                        // @ts-ignore
                        'X-XSRF-TOKEN': auth.getXsrfToken()
                    }
                }
            ).catch(
                (error) => {
                    console.log(error)
                    // @ts-ignore
                    auth.authError(error.response.status)
                }
            )

            const ordersData = response?.data;


            if (ordersData === undefined || ordersData.length <= 0) {
                swal("No orders found!", "", "warning")
                setTotalPages(1)
                setPage(1)
                setOrders([])
                return
            }

            if (Array.isArray(ordersData)) {
                setOrders(ordersData);

                const newTotalPages = Math.ceil(ordersData[0].totalOrdersMatchingRequest / size);
                setTotalPages(newTotalPages);

                if (pageNumber > newTotalPages) {
                    setPage(newTotalPages);
                }
            } else {
                console.error('Invalid response format:', ordersData);
            }
        } catch (error) {
            console.error('Error fetching data:', error);

            // @ts-ignore
            if (error.response === undefined) {
                await swal("Error fetching data!", "", "error")
                return
            }

            // @ts-ignore
            auth.authError(error.response.status)

            // @ts-ignore
            if (error.response.status === 404) {
                setOrders([]);
            }

        }
        console.log("Fetch orders Status: " + status + " Page: " + pageNumber + " PageSize: " + size);
    }

    const handleSubmit = (event: any) => {

        event.preventDefault();
        setPreviousStatus(selectedStatus)
        setPreviousUser(selectedUser)
        setResetKey(selectedStatus + selectedUser)
        // @ts-ignore
        setPage(1);
        setPageSize(pageSize);
        fetchOrders(1, pageSize, selectedStatus, selectedUser);
    }


    const handlePageChange = (pageNumber: number, size: number) => {

        if (previousStatus !== selectedStatus || previousUser !== selectedUser) {
            console.log("Status changed")
            setPage(1);

            setPreviousStatus(selectedStatus)
            setPreviousUser(selectedUser)
            // @ts-ignore
            fetchOrders(1, pageSize, selectedStatus, selectedUser)
            return
        }

        setPageSize(size)
        setPage(pageNumber)
        setResetKey(selectedStatus + selectedUser)

        fetchOrders(pageNumber, size, selectedStatus, selectedUser)


        console.log("Page change: " + pageNumber + " PageSize: " + size)
    }

    return (
        <>
            <NavigationBar/>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                 className={"all-orders"}>
                <h1>ALL ORDERS</h1>
                <div
                    className={"all-orders-search w-100 row text-center"}>
                    <div className={"search-bar col-12 col-xl-4 status-box"}>
                        <div className={"row w-100"}>
                            <h4 className={"col-12 col-xl-4"}>Filter by status</h4>
                            <div className={"col-12 col-xl-8 dropdown-status-box"}>
                                <Dropdown
                                    label={selectedStatus.charAt(0).toUpperCase() + selectedStatus.slice(1)}
                                    options={["All", "Completed", "Pending", "Cancelled", "In progress"]}
                                    onSelect={(option: any) => {
                                        setPreviousStatus(selectedStatus)
                                        setSelectedStatus(option)
                                    }}
                                />
                            </div>
                        </div>
                    </div>
                    <div className={"user-search-order col-12 col-xl-5"}>
                        <div className={"row"}>

                            <h4 className={"col-12 col-xl-2"}>Search for user</h4>
                            <div className={"col-12 col-xl-6"}>
                                <form onSubmit={getUsers} className={"w-100 "}>
                                    <Dropdown
                                        label={selectedSearchFieldUser.charAt(0).toUpperCase() + selectedSearchFieldUser.slice(1)}
                                        options={["name", "email", "username"]}
                                        onSelect={(option: any) => {
                                            setSelectedSearchFieldUser(option)
                                        }}
                                    />
                                    <input required name={"search-value"} id={"search-value"} type="text"
                                           placeholder="Search for user"/>
                                    <button type={"submit"}>
                                        <img src={searchIcon} alt={"Search"}/>
                                    </button>

                                </form>
                            </div>
                            <div className={"col-12 col-xl-4"}>
                                <button onClick={
                                    () => {
                                        setSelectedUser("")
                                    }
                                }
                                        className={"btn-style m-3 m-xl-0 user-selection"}
                                >
                                    Clear Selection
                                </button>
                            </div>
                        </div>
                    </div>
                    <div className={"col-12 col-xl-3"}>

                    </div>
                </div>

                <div className={"all-orders-users"}>
                    <table className={"overflow-scroll text-center"}>
                        <tbody>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Username</th>
                        </tr>

                        {users.map((user: any) => (
                            <tr key={user.user_id} onClick={
                                () => {
                                    console.log(user)
                                    setPreviousUser(selectedUser)

                                    setSelectedUser(user.user_id)
                                }
                            }
                                className={"user-row " + (selectedUser === user.user_id ? "selected-row" : "")}
                            >
                                <td>{user.name || "Not Applicable"}</td>
                                <td>{user.email || "Not Applicable"}</td>
                                <td>{user.username || "Not Applicable"}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                </div>
                <form onSubmit={handleSubmit} className={"m-4"}>
                    <button type={"submit"} className={"user-selection"}>Search With Applied Filters</button>
                </form>
                {orders.map(order => (
                    <OrderTile
                        key={order.orderTrackingNumber}
                        order={{
                            orderTrackingNumber: order.orderTrackingNumber,
                            orderId: order.orderId,
                            service: order.service,
                            customerFullName: order.customerFullName,
                            orderStatus: order.orderStatus,
                        }}
                        action={() => {

                        }}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages}
                                  resetKey={resetKey}/>

            </div>
        </>
    );
}

export default AllOrdersPage;
