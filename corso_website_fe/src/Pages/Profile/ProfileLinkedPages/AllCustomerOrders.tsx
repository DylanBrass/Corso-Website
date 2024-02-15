import React, {useEffect, useState} from "react";
import axios from "axios";
import OrderTile, {Order} from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useAuth} from "../../../security/Components/AuthProvider";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";

function AllCustomerOrders() {

    const auth = useAuth();
    const [t]= useTranslation()

    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize,] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {
            await axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders?pageSize=${size}&offset=${(pageNumber - 1) * size}`)
                .then(res => {
                    const orders = res.data;

                    if (res.data[0] === undefined) {
                        return;
                    }
                    setOrders(orders);

                    const newTotalPages = Math.ceil(orders[0].totalOrdersMatchingRequest / size);
                    setTotalPages(newTotalPages);

                    if (pageNumber > newTotalPages) {
                        setPage(newTotalPages);
                    }
                }).catch(err => {
                

                if (err.response.status === 401 || err.response.status === 403) {
                    // @ts-ignore
                    auth.authError(err.response.status)
                }
            })


        } catch (error) {
            

        }

    };

    const handlePageChange = (pageNumber: number, size: number) => {
        setPage(pageNumber);
        fetchOrders(pageNumber, size)
    };

    useEffect(() => {
        fetchOrders(page, pageSize)
            // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page, pageSize]);


    const backButtonHref = "/";
    return (
        <div>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToMainPage")}
            />
            <div className={"mb-5 container"}>
                <div
                     className={"w-100"}>

                    {orders.length <= 0 &&
                        <div className="row text-center">
                            <div className="col-12">
                                <h1 className="error">{t("order.noOrdersForCustomer")} <a
                                    href={"/create/orders"}>{t("here")}</a></h1>
                            </div>
                        </div>
                    }
                    {orders.length > 0 &&
                        <div className={"w-100 d-flex flex-column align-items-center"}>

                            {
                                orders.map((order: Order) => (
                                        <OrderTile order={order}
                                                   action={() => {
                                                       window.location.href = `/orders/${order.orderId}`
                                                   }} key={order.orderId}/>
                                ))
                            }
                            <CustomPagination onPageChange={handlePageChange} pageSize={pageSize}
                                              totalPages={totalPages}/>
                        </div>
                    }
                </div>
            </div>
        </div>
    )
}

export default AllCustomerOrders
