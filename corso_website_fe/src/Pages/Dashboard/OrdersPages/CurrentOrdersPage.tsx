import React, { useState, useEffect } from 'react';
import axios from 'axios';
import OrderTile from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";
import { Order } from "../../../Components/OrderTile/OrderTile";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";

function CurrentOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const [t] = useTranslation();

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {

            await axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders?status=in_progress&pageSize=${size}&offset=${(pageNumber - 1) * size}`)
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
            
        }
    };

    const handlePageChange = (pageNumber: number, size: number) => {
        setPage(prevPage => pageNumber);
        setPageSize(size);
    };

    useEffect(() => {
        fetchOrders(page, pageSize);
        
    }, [page, pageSize]);

    const backButtonHref = "/dashboard";

    return (
        <>
            <NavigationBar />
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <h1>{t("dashboardPage.currentOrders").toUpperCase()}</h1>
                {orders.map(order => (
                    <OrderTile
                        key={order.orderTrackingNumber}
                        action={() => {
                            window.location.href = `/manage/currentOrder/${order.orderId}`
                        }
                        }
                        order={{
                            orderTrackingNumber: order.orderTrackingNumber,
                            orderId: order.orderId,
                            service: order.service,
                            customerFullName: order.customerFullName,
                            orderStatus: order.orderStatus,
                        }}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages} />
            </div>
        </>
    );
}

export default CurrentOrdersPage;
