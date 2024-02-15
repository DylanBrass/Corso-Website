import React, {useState, useEffect} from 'react';
import axios from 'axios';
import OrderTile, {Order} from "../../../Components/OrderTile/OrderTile";
import NavigationBar from "../../../Components/NavBar/NavigationBar";
import CustomPagination from "../../../Components/Pagination";
import {useTranslation} from "react-i18next";
import BackButton from "../../../Components/BackButton";



function PendingOrdersPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [page, setPage] = useState(1);
    const [pageSize,] = useState(10);
    const [totalPages, setTotalPages] = useState(1);
    const {t} = useTranslation();

    const fetchOrders = async (pageNumber: number, size: number) => {
        try {


            axios.get(process.env.REACT_APP_BE_HOST + `api/v1/corso/orders?status=pending&pageSize=${size}&offset=${(pageNumber - 1) * size}`)
                .then(res => {
                    const orders = res.data;
                    setOrders(orders);

                    if (res.data.length <= 0) {
                        setPage(1);
                        return
                    }

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
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                 className={"mb-5"}>
                <h1>{t("dashboardPage.pendingOrders").toUpperCase()}</h1>
                {
                    orders.map((order: Order) => (
                        <OrderTile order={order}
                                   action={() => {
                                       window.location.href = `/manage/orders/${order.orderId}`
                                   }
                                   }
                                   key={order.orderId}/>
                    ))
                }
                <CustomPagination onPageChange={handlePageChange} pageSize={pageSize} totalPages={totalPages}/>
            </div>
        </>
    );
}

export default PendingOrdersPage;