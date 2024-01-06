
import React from 'react';
import './OrderTile.css';

export {};

export class Order {
    orderTrackingNumber: string;
    orderId: string;
    serviceName: string;
    customerFullName: string;
    orderStatus: string;
    action: () => void;


    constructor(orderTrackingNumber: string, orderId: string, serviceName: string, customerFullName: string, orderStatus: string, action: () => void) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderId = orderId;
        this.serviceName = serviceName;
        this.customerFullName = customerFullName;
        this.orderStatus = orderStatus;
        this.action = action;
    }
}

function OrderTile({ order }: { order: Order }) {
    // Get the first 4 characters of orderId
    if(order.orderStatus === "in_progress") {
        order.orderStatus = "in progress";
    }

    return (
        <div className="orders-tile" onClick={order.action}>
            <div className="box-body">
                <div className={"order-data-separation"}>
                    <p className="order-id"><b>ORDER NUMBER</b> {order.orderTrackingNumber}</p>
                    <p className={"customer-full-name"}><b>CUSTOMER </b>: {order.customerFullName}</p>
                </div>
                <div className={"order-data-separation"}>
                    <p className="order-service"><b>Service</b>: {order.serviceName}</p>
                    <p className="order-status"><b>Order Status</b>: {order.orderStatus}</p>
                </div>
            </div>
        </div>
    );
}

export default OrderTile;
