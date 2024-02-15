import React, {useEffect, useState} from 'react';
import axios from 'axios';
import OrderTile, {OverdueOrder} from "../../../Components/OrderTile/OrderTile";
import CustomPagination from "../../../Components/Pagination";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import {format, parseISO} from 'date-fns';
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";


function OverdueOrdersPage() {
    const [orders, setOrders] = useState<OverdueOrder[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize,] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const {t} = useTranslation();

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {


            await axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders/overdue?pageSize=${size}&offset=${(pageNumber - 1) * size}`)
                .then(res => {

                    const newOrders = res.data.map((order: any) => {
                        const dueDate = parseISO(order.dueDate);

                        // Ensure current date is in UTC
                        const currentDate = new Date();
                        const currentDateUTC = format(currentDate, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");


                        const overdueDays = Math.floor((new Date(currentDateUTC).getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24));

                        return new OverdueOrder(
                            order.orderTrackingNumber,
                            order.orderId,
                            order.service,
                            order.customerFullName.toUpperCase(),
                            order.orderStatus.toLowerCase(),
                            overdueDays
                        )
                    })


                    setOrders(newOrders);

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
        setPage(pageNumber);
        fetchOrders(pageNumber, size);
    };

    useEffect(() => {
        fetchOrders(page, pageSize);
    }, [page, pageSize]);

    const backButtonHref = "/dashboard";

    return (
        <>
            <NavigationBar/>
            <BackButton
                link={backButtonHref}
                text={ t("backToDashboard")}
            />
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}>
                <h1>{t("dashboardPage.lateOrders")}</h1>
                {orders.map(order => (
                    <OrderTile
                        action={() => {
                            window.location.href = "/manage/currentOrder/" + order.orderId
                        }
                        }
                        key={order.orderTrackingNumber}
                        order={order}
                    />
                ))}
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages}/>
            </div>
        </>
    );
}

export default OverdueOrdersPage;
