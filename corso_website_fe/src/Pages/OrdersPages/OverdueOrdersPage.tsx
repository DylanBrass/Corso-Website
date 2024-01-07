import React, { useState, useEffect } from 'react';
import axios from 'axios';
import OrderTile, {OverdueOrder} from "../../Components/OrderTile/OrderTile";
import CustomPagination from "../../Components/Pagination";
import { Order } from "../../Components/OrderTile/OrderTile";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import { format, parseISO } from 'date-fns';


function OverdueOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize, ] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {
            const responseTotalOrders = await axios.get(`http://localhost:8080/api/v1/corso/orders/overdue?pageSize=0&offset=0`);
            const nbOrdersTotal = responseTotalOrders.data.length;

            const newTotalPages = Math.ceil(nbOrdersTotal / size);

            const response = await axios.get(`http://localhost:8080/api/v1/corso/orders/overdue?pageSize=${size}&offset=${(pageNumber - 1) * size}`);
            console.log(response.data)
            const responseData = response.data;

            const newOrders = await Promise.all(responseData.map(async (order: any) => {
                const serviceResponse = await axios.get("http://localhost:8080/api/v1/corso/services/" + order.serviceId);
                console.log("SERVICES " + serviceResponse.data);
                const serviceName = serviceResponse.data.serviceName;

                const dueDate = parseISO(order.dueDate);

                // Ensure current date is in UTC
                const currentDate = new Date();
                const currentDateUTC = format(currentDate, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


                const overdueDays = Math.floor((new Date(currentDateUTC).getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24));

                console.log("current date " + currentDate);
                console.log("due date " + dueDate);
                console.log("overdue days " + overdueDays);

                return new OverdueOrder(
                    order.orderTrackingNumber,
                    serviceName,
                    order.customerFullName.toUpperCase(),
                    order.orderStatus.toLowerCase(),
                    () => {
                        console.log("Order clicked");
                    },
                    overdueDays
                );
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
    }, [page, pageSize]);

    return (
        <>
            <NavigationBar/>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <h1>OVERDUE ORDERS</h1>
                {orders.map(order => (
                    <OrderTile
                        key={order.orderTrackingNumber}
                        order={order}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages} />
            </div>
        </>
    );
}

export default OverdueOrdersPage;
