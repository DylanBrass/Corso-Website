import React, { useState, useEffect } from 'react';
import axios from 'axios';
import OrderTile from "../../Components/OrderTile/OrderTile";
import CustomPagination from "../../Components/Pagination";
import { Order } from "../../Components/OrderTile/OrderTile";
import NavigationBar from "../../Components/NavBar/NavigationBar";
import Dropdown from "../../Components/Dropdown/droptown";

function AllOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [selectedSearchField, setSelectedSearchField] = useState("all");

    useEffect(() => {
        fetchOrders(page, pageSize, selectedStatus);
    }, [page, pageSize, selectedStatus]);

    const fetchOrders = async (pageNumber: number, size: number, status: string | null) => {
        try {
            let url = `http://localhost:8080/api/v1/corso/orders/filter?pageSize=${size}&offset=${(pageNumber - 1) * size}`;

            if (status !== null && status.toLowerCase() !== "all") {
                status = status.replace(" ", "_");
                url += `&status=${status}`;
            }

            const response = await axios.get(url);
            const ordersData = response.data;

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
        }
        console.log("Fetch orders Status: " + status + " Page: " + pageNumber + " PageSize: " + size);
    };

    const handleStatusChange = async (status: string) => {
        // @ts-ignore
        setSelectedStatus(status);
        setPage(1);
        setPageSize(1);
        const updatedPageSize = 1;
        fetchOrders(1, updatedPageSize, status);
    };


    const handlePageChange = (pageNumber: number, size: number) => {
        setPage(pageNumber);
        fetchOrders(pageNumber, size, selectedStatus);
        console.log("Page change: " + pageNumber + " PageSize: " + size);
    };

    return (
        <>
            <NavigationBar />
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <h1>ALL ORDERS</h1>
                <Dropdown
                    label={selectedSearchField.charAt(0).toUpperCase() + selectedSearchField.slice(1)}
                    options={["All", "Completed", "Pending", "Cancelled", "In progress"]}
                    onSelect={(option: any) => {
                        setSelectedSearchField(option);
                        handleStatusChange(option);
                    }}
                />
                {orders.map(order => (
                    <OrderTile
                        key={order.orderTrackingNumber}
                        order={{
                            orderTrackingNumber: order.orderTrackingNumber,
                            orderId: order.orderId,
                            service: order.service,
                            customerFullName: order.customerFullName,
                            orderStatus: order.orderStatus,
                            action: order.action
                        }}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages} resetKey={selectedStatus} />

            </div>
        </>
    );
}

export default AllOrdersPage;
