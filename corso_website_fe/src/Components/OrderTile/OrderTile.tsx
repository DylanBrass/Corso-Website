
import React from 'react';
import './OrderTile.css';
import Service from "../../Models/Service";

export {};

export class Order {
    orderTrackingNumber: string;
    orderId: string;
    service: Service;
    customerFullName: string;
    orderStatus: string;
    action: () => void;


    constructor(orderTrackingNumber: string, orderId: string, service: Service, customerFullName: string, orderStatus: string, action: () => void) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderId = orderId;
        this.service = service;
        this.customerFullName = customerFullName;
        this.orderStatus = orderStatus;
        this.action = action;
    }
}

export class OverdueOrder extends Order {
    overdueDays: number;

    constructor(
        orderTrackingNumber: string,
        orderId: string,
        service: Service,
        customerFullName: string,
        orderStatus: string,
        action: () => void,
        overdueDays: number
    ) {
        super(orderTrackingNumber, orderId, service, customerFullName, orderStatus, action);
        this.overdueDays = overdueDays;
    }
}


function OrderTile({ order }: { order: Order }) {
    if(order instanceof OverdueOrder) {
        order.orderStatus = "overdue";
    }

    order.orderStatus = order.orderStatus.replace("_", " ").toLowerCase();
    // Get the first 4 characters of orderId


    return (
        <div className="orders-tile" onClick={order.action}>
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
