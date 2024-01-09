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
        fetchOrders(page, pageSize, selectedStatus);
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


    const fetchOrders = async (pageNumber: number, size: number, status: string | null) => {
        try {


            let url = `http://localhost:8080/api/v1/corso/orders/filter?pageSize=${size}&offset=${(pageNumber - 1) * size}`;
            if (status !== null && status.toLowerCase() !== "all") {
                status = status.replace(" ", "_");
                url += `&status=${status}`;

            }

            if (selectedUser.length > 0) {
                // @ts-ignore
                url += `&userId=${selectedUser.replace("|", "%7C")}`;
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
        setResetKey(selectedStatus + selectedUser)
        // @ts-ignore
        setPage(1);
        setPageSize(1);
        const updatedPageSize = 1;
        fetchOrders(1, updatedPageSize, selectedStatus);
    }


    const handlePageChange = (pageNumber: number, size: number) => {


        if (previousStatus !== selectedStatus || previousUser !== selectedUser) {
            setPreviousStatus(selectedStatus)
            setResetKey(selectedStatus + selectedUser)
            // @ts-ignore
            setPage(1);
            setPageSize(1);
            const updatedPageSize = 1;
            fetchOrders(1, updatedPageSize, selectedStatus);
            return
        }
        setPageSize(size);
        setPage(pageNumber)
        fetchOrders(pageNumber, size, selectedStatus);


        console.log("Page change: " + pageNumber + " PageSize: " + size)
    };

    return (
        <>
            <NavigationBar/>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                 className={"all-orders"}>
                <h1>ALL ORDERS</h1>
                <div style={{display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}
                     className={"all-orders-search"}>
                    <Dropdown
                        label={selectedStatus.charAt(0).toUpperCase() + selectedStatus.slice(1)}
                        options={["All", "Completed", "Pending", "Cancelled", "In progress"]}
                        onSelect={(option: any) => {
                            setPreviousStatus(selectedStatus)
                            setSelectedStatus(option)
                        }}
                    />
                    <div className={"search-bar"}>
                        <form onSubmit={getUsers}>
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
                    <form onSubmit={handleSubmit}>
                        <button type={"submit"}>Search</button>
                    </form>
                </div>

                <div className={"all-orders-users"}>
                    <table className={"overflow-scroll"}>
                        <tbody>
                        <tr>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Name</th>
                        </tr>
                        {users.map((user: any) => (
                            <tr key={user.username} onClick={
                                () => {
                                    console.log(user)
                                    setPreviousUser(selectedUser)
                                    setSelectedUser(user.user_id)
                                }
                            }>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.name}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <button onClick={
                        () => {
                            setSelectedUser("")
                        }
                    }
                            className={"mt-1 mb-5 btn-style"}
                    >
                        Clear User Selection
                    </button>
                </div>

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
