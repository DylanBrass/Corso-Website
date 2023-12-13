package com.corso.springboot.Order_Subdomain.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class OrderIdentifier {

    private String orderId;

    public OrderIdentifier() {
        this.orderId = java.util.UUID.randomUUID().toString();
    }

    public String getOrderId() {
        return orderId;
    }
}