
import React from 'react';
import './OrderTile.css';
import ServiceOrder from "../../Models/ServiceOrder";

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
    if(order instanceof OverdueOrder) {
        order.orderStatus = "overdue";
    }

    order.orderStatus = order.orderStatus.replace("_", " ").toLowerCase();
    // Get the first 4 characters of orderId


    return (
        <div className="orders-tile" onClick={action}>
            <div className="box-body">
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-id"><b>ORDER NUMBER</b> {order.orderTrackingNumber}</p>
                    <p className={"customer-full-name"}><b>CUSTOMER </b>: {order.customerFullName}</p>
                </div>
                <div className={"order-data-separation col-6 text-start"}>
                    <p className="order-service text-capitalize"><b>Service</b>: {order.service.serviceName}</p>
                    <p className="order-status text-capitalize"><b>Order Status</b>: {order.orderStatus}</p>
                    {order instanceof OverdueOrder && (
                        <>
                            <p className="order-days"><b>Overdue by</b>: {order.overdueDays} days</p>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default OrderTile;
