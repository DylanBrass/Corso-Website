
import React, {useEffect} from 'react';
import './OrderTile.css';
import ServiceOrder from "../../ressources/Models/ServiceOrder";
import {useTranslation} from "react-i18next";
import i18n from "i18next";
import {getValuesFromJSON} from "../../Services/TranslationTools";

export {};

export class Order {
    orderTrackingNumber: string;
    orderId: string;
    service: ServiceOrder;
    customerFullName: string;
    orderStatus: string;


    constructor(orderTrackingNumber: string, orderId: string, service: ServiceOrder, customerFullName: string, orderStatus: string) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderId = orderId;
        this.service = service;
        this.customerFullName = customerFullName;
        this.orderStatus = orderStatus;
    }
}

export class OverdueOrder extends Order {
    overdueDays: number;

    constructor(
        orderTrackingNumber: string,
        orderId: string,
        service: ServiceOrder,
        customerFullName: string,
        orderStatus: string,
        overdueDays: number
    ) {
        super(orderTrackingNumber, orderId, service, customerFullName, orderStatus);
        this.overdueDays = overdueDays;
    }
}


function OrderTile({ order, action}: { order: Order, action: () => void }) {
    const [t] = useTranslation();
    const [orderStatus, setOrderStatus] = React.useState<string>(order.orderStatus);

    if(order instanceof OverdueOrder) {
        order.orderStatus = "overdue";
    }

    useEffect(() => {
        if(i18n.language === "fr") {
            switch(order.orderStatus.toLowerCase()) {
                case "pending":
                    setOrderStatus("en attente");
                    break;
                case "completed":
                    setOrderStatus("terminée");
                    break;
                case "overdue":
                    setOrderStatus("en retard");
                    break;
                case "in_progress":
                    setOrderStatus("en cours");
                    break;
                case "cancelled":
                    setOrderStatus("annulée");
                    break;
                case "declined":
                    setOrderStatus("refusée");
                    break;
            }
        } else {
            order.orderStatus = order.orderStatus.replace("_", " ").toLowerCase();
            setOrderStatus(order.orderStatus)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [i18n.language, orderStatus])

    return (
        <div className="orders-tile" onClick={action}>
            <div className="box-body">
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-id"><b>{t("order.orderNumber").toUpperCase()}</b> {order.orderTrackingNumber}</p>
                    <p className={"customer-full-name"}><b>{t("customerInfo.customer").toUpperCase()} </b>: {order.customerFullName}</p>
                </div>
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-service text-capitalize"><b>Service</b>: {`${getValuesFromJSON(i18n.language, order.service.serviceName)}`}</p>
                    <p className="order-status"><b>{t("order.orderStatus")}</b>: {orderStatus}</p>
                    {order instanceof OverdueOrder && (
                        <>
                            <p className="order-days"><b>{t("order.overdueBy")}</b>: {order.overdueDays} {t("order.days")}</p>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default OrderTile;
