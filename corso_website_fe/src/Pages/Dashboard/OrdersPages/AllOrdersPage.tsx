import React, {useEffect, useState} from 'react';
import axios from 'axios';
import OrderTile, {Order} from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import Dropdown from "../../../Components/Dropdown/droptown";
import swal from "sweetalert";
import searchIcon from "../../../ressources/images/Search_light.svg";
import {useAuth} from "../../../security/Components/AuthProvider";
import "./AllOrdersPage.css"
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";
import '../../globalStyling/globalStyling.css';

function AllOrdersPage() {

    const auth: {} = useAuth()

    const {t, i18n} = useTranslation();

    const isFrench = i18n.language === 'fr';

    const [orders, setOrders] = useState<Order[]>([]);

    const [page, setPage] = useState(1);

    const [pageSize, setPageSize] = useState(10);

    const [totalPages, setTotalPages] = useState(1);

    const [selectedStatus, setSelectedStatus] = useState(t("allOrdersAdminPage.statusOptions.all"));

    const [previousStatus, setPreviousStatus] = useState(t("allOrdersAdminPage.statusOptions.all"));

    const [selectedUser, setSelectedUser] = useState("");

    const [previousUser, setPreviousUser] = useState("");

    let [selectedSearchFieldUser, setSelectedSearchFieldUser] = useState(t("userOptions.email"));

    const [resetKey, setResetKey] = useState<string | null>(null);

    useEffect(() => {
        fetchOrders(page, pageSize, selectedStatus, selectedUser);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page, pageSize]);

    const [users, setUsers] = React.useState([])

    useEffect(() => {
        setSelectedSearchFieldUser(t("userOptions.email"));
        setSelectedStatus(t("allOrdersAdminPage.statusOptions.all"));
    }, [i18n.language, t]);

    const getUsers = async (event?: React.FormEvent<HTMLFormElement>) => {

        if (event) {
            event.preventDefault()

            if ((document.getElementById("search-value") as HTMLInputElement).value.replaceAll(/\s/g, '').length < 3) {
                swal((t("alerts.enter3CharMinWarning")), "", "warning")
                return
            }

            if (isFrench) {
                switch (selectedSearchFieldUser) {
                    case "courriel":
                        selectedSearchFieldUser = "email";
                        break;
                    case "nom":
                        selectedSearchFieldUser = "name";
                        break;
                    case "nom d'utilisateur":
                        selectedSearchFieldUser = "username";
                        break;
                    default:
                        selectedSearchFieldUser = "email";
                        break;
                }
            }

            let param = "?" + selectedSearchFieldUser + "=" + (document.getElementById("search-value") as HTMLInputElement).value

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

    }

    const fetchOrders = async (pageNumber: number, size: number, status: string | null, user: string) => {
        if (isFrench) {
            switch (status) {
                case "Tous":
                    status = "all";
                    break;
                case "Terminé":
                    status = "completed";
                    break;
                case "En cours":
                    status = "in_progress";
                    break;
                case "En attente":
                    status = "pending";
                    break;
                case "Refusé":
                    status = "declined";
                    break;
                case "Annulé":
                    status = "cancelled";
                    break;
                default:
                    status = "all";
                    break;
            }
        }

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

                    // @ts-ignore
                    auth.authError(error.response.status)
                }
            )

            const ordersData = response?.data;


            if (ordersData === undefined || ordersData.length <= 0) {
                swal(t("alerts.order.noOrdersFound"), "", "warning")
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

            }
        } catch (error) {


            // @ts-ignore
            if (error.response === undefined) {
                await swal(t("alerts.errorFetchingData"), "", "error")
                return
            }

            // @ts-ignore
            auth.authError(error.response.status)

            // @ts-ignore
            if (error.response.status === 404) {
                setOrders([]);
            }

        }

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


    }

    const backButtonHref = "/dashboard";

    return (
        <>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                 className={"all-orders"}>
                <h1>{t("allOrdersAdminPage.allOrders")}</h1>
                <div
                    className={"all-orders-search w-100 row text-center"}>
                    <div className={"search-bar col-12 col-xl-4 status-box"}>
                        <div className={"row w-100"}>
                            <h4 className={"col-12 col-xl-4"}>{t("allOrdersAdminPage.filterByStatus")}</h4>
                            <div className={"col-12 col-xl-8 dropdown-status-box"}>
                                <Dropdown
                                    label={selectedStatus.charAt(0).toUpperCase() + selectedStatus.slice(1)}
                                    options={[
                                        t("allOrdersAdminPage.statusOptions.all"),
                                        t("allOrdersAdminPage.statusOptions.completed"),
                                        t("allOrdersAdminPage.statusOptions.pending"),
                                        t("allOrdersAdminPage.statusOptions.cancelled"),
                                        t("allOrdersAdminPage.statusOptions.inProgress"),
                                        t("allOrdersAdminPage.statusOptions.declined")
                                    ]}
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

                            <h4 className={"col-12 col-xl-2"}>{t("userOptions.searchForUser")}</h4>
                            <div className={"col-12 col-xl-6"}>
                                <form onSubmit={getUsers} className={"w-100 "}>
                                    <Dropdown
                                        label={selectedSearchFieldUser.charAt(0).toUpperCase() + selectedSearchFieldUser.slice(1)}
                                        options={[
                                            t("userOptions.email"),
                                            t("userOptions.name"),
                                            t("userOptions.username"),
                                        ]}
                                        onSelect={(option: any) => {
                                            setSelectedSearchFieldUser(option)
                                        }}
                                    />
                                    <input required name={"search-value"} id={"search-value"} type="text"
                                           placeholder={t("userOptions.searchForUser")}/>
                                    <button  type={"submit"}>
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
                                    {t("userOptions.clearSelection")}
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
                            <th>{t("userOptions.name")}</th>
                            <th>{t("userOptions.email")}</th>
                            <th>{t("userOptions.username")}</th>
                        </tr>

                        {users.map((user: any) => (
                            <tr key={user.user_id} onClick={
                                () => {

                                    setPreviousUser(selectedUser)

                                    setSelectedUser(user.user_id)
                                }
                            }
                                className={"user-row " + (selectedUser === user.user_id ? "selected-row" : "")}
                            >
                                <td>{user.name || (t("userOptions.notApplicable"))}</td>
                                <td>{user.email || (t("userOptions.notApplicable"))}</td>
                                <td>{user.username || (t("userOptions.notApplicable"))}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                </div>
                <form onSubmit={handleSubmit} className={"m-4"}>
                    <button type={"submit"}
                            className={"btn-style user-selection"}>{t("allOrdersAdminPage.searchWithAppliedFilters")}</button>
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
                            if (order.orderStatus.toLowerCase() === "pending") {
                                window.location.href = "/manage/orders/" + order.orderId
                            } else if (order.orderStatus.toLowerCase() === "in_progress") {
                                window.location.href = "/manage/currentOrder/" + order.orderId
                            } else if (order.orderStatus.toLowerCase() === "declined"
                                || order.orderStatus.toLowerCase() === "cancelled"
                                || order.orderStatus.toLowerCase() === "completed"
                            ) {
                                window.location.href = "/manage/orders/inactive/" + order.orderId
                            }
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
