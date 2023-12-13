import React, { useState, useEffect } from 'react';
import axios from 'axios';
import OrderTile from "../Components/OrderTile";
import CustomPagination from "../Components/Pagination";
import { Order } from "../Components/OrderTile";
import NavigationBar from "../Components/NavigationBar";

function CurrentOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {
            const responseTotalOrders = await axios.get(`http://localhost:8080/api/v1/corso/orders?status=in_progress&pageSize=0&offset=0`);
            const nbOrdersTotal = responseTotalOrders.data.length;

            const newTotalPages = Math.ceil(nbOrdersTotal / size);

            const response = await axios.get(`http://localhost:8080/api/v1/corso/orders?status=in_progress&pageSize=${size}&offset=${(pageNumber - 1) * size}`);
            console.log(response.data)
            const responseData = response.data;

            const newOrders = await Promise.all(responseData.map(async (order: any) => {
                const serviceResponse = await axios.get("http://localhost:8080/api/v1/corso/services/" + order.serviceId);
                console.log("SERVICES " + serviceResponse.data);
                const serviceName = serviceResponse.data.serviceName;

                return new Order(order.orderTrackingNumber, serviceName, order.customerFullName.toUpperCase(), order.orderStatus.toLowerCase(), () => {
                    console.log("Order clicked");
                });
            }));

            setOrders(newOrders);
            setTotalPages(newTotalPages);

            if (pageNumber > newTotalPages) {
                setPage(newTotalPages);
            }
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
    }, []);

    return (
        <>
            <NavigationBar/>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <h1>CURRENT ORDERS</h1>
                {orders.map(order => (
                    <OrderTile
                        key={order.orderTrackingNumber}
                        order={{
                            orderTrackingNumber: order.orderTrackingNumber,
                            serviceName: order.serviceName,
                            customerFullName: order.customerFullName,
                            orderStatus: order.orderStatus,
                            action: order.action
                        }}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages} />
            </div>
        </>
    );
}

export default CurrentOrdersPage;
