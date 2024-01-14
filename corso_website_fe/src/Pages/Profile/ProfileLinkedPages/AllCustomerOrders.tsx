import React, {useEffect, useState} from "react";
import axios from "axios";
import OrderTile, {Order} from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";
import NavigationBar from "../../../Components/NavBar/NavigationBar";

function AllCustomerOrders() {

    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize,] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {
            axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/customers/orders?pageSize=${size}&offset=${(pageNumber - 1) * size}`)
                .then(res => {
                    const orders = res.data;
                    setOrders(orders);

                    if (res.data[0] === undefined) {
                        return;
                    }
                    const newTotalPages = Math.ceil(res.data[0].totalOrdersMatchingRequest / size);
                    setTotalPages(newTotalPages);
                    if (pageNumber > newTotalPages) {
                        setPage(newTotalPages);
                    }
                }).catch(err => {
                console.log(err);
            })


        } catch (error) {
            console.error('Error fetching data:', error);
        }

    };

    const handlePageChange = (pageNumber: number, size: number) => {
        setPage(pageNumber);
        fetchOrders(pageNumber, size).then(r => console.log(r))
    };

    useEffect(() => {
        fetchOrders(page, pageSize).then(r => console.log(r))
    }, [page, pageSize]);

    return (
        <div>
            <NavigationBar/>
            <div className={"mb-5"}>
                <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                     className={"w-100"}>

                    {orders.length <= 0 &&
                        <div className="row">
                            <div className="col-12">
                                <h1 className="error">No orders found, you can request one <a href={"/create/orders"}>here</a></h1>
                            </div>
                        </div>
                    }
                    {orders.length > 0 &&
                        <div>
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
