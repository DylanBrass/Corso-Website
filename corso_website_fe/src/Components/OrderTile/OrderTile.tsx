
import React from 'react';
import './OrderTile.css';
import ServiceOrder from "../../ressources/Models/ServiceOrder";
import {useTranslation} from "react-i18next";

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
    if(order instanceof OverdueOrder) {
        order.orderStatus = "overdue";
    }

    order.orderStatus = order.orderStatus.replace("_", " ").toLowerCase();

    return (
        <div className="orders-tile" onClick={action}>
            <div className="box-body">
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-id"><b>{t("order.orderNumber").toUpperCase()}</b> {order.orderTrackingNumber}</p>
                    <p className={"customer-full-name"}><b>{t("customerInfo.customer").toUpperCase()} </b>: {order.customerFullName}</p>
                </div>
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-service text-capitalize"><b>Service</b>: {order.service.serviceName}</p>
                    <p className="order-status"><b>{t("order.orderStatus")}</b>: {order.orderStatus}</p>
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
