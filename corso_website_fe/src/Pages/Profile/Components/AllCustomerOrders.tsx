import React, {useEffect, useState} from "react";
import axios from "axios";
import OrderTile from "../../../Components/OrderTile/OrderTile";
import {Order} from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";

function AllCustomerOrders() {

    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize,] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {
            axios.get(`http://localhost:8080/api/v1/corso/customers/orders?pageSize=${size}&offset=${(pageNumber - 1) * size}`)
                .then(res => {
                    const orders = res.data;
                    setOrders(orders);

                    const newTotalPages = Math.ceil(res.data[0].totalOrdersMatchingRequest / size);
                    setTotalPages(newTotalPages);
                    if (pageNumber > newTotalPages) {
                        setPage(newTotalPages);
                    }
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
            <div className={"mb-5"}>
                <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}} className={"w-100"}>

                    {
                        orders.map((order: Order) => (
                            <OrderTile order={order} key={order.orderId}/>
                        ))
                    }
                    <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages}/>
                </div>
            </div>
        </div>
    )
}

export default AllCustomerOrders
