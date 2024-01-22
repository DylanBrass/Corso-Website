package com.corso.springboot.Order_Subdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class OrderIdentifier {

    private String orderId;

    public OrderIdentifier() {
        this.orderId = java.util.UUID.randomUUID().toString();
    }

}