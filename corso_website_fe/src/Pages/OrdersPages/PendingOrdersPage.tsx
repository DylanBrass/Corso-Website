import React, { useState, useEffect } from 'react';
import axios from 'axios';
import OrderTile, {Order} from "../../Components/OrderTile/OrderTile";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import CustomPagination from "../../Components/Pagination";


function PendingOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize, ] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {


            axios.get(`http://localhost:8080/api/v1/corso/orders?status=pending&pageSize=${size}&offset=${(pageNumber - 1) * size}`)
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
        fetchOrders(pageNumber, size);
    };

    useEffect(() => {
        fetchOrders(page, pageSize);
    }, [page, pageSize]);

    return (
        <>
            <NavigationBar/>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }} className={"mb-5"}>
                <h1>CLIENT REQUESTS</h1>
                {
                    orders.map((order: Order) => (
                        <OrderTile order={order} key={order.orderId}/>
                    ))
                }
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages} />

            </div>
        </>
    );
}

export default PendingOrdersPage;