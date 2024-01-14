package com.corso.springboot.Customer_Subdomain.presentationlayer;


import lombok.*;

@Value
@Builder
public class CustomerCancelOrderRequest {

    public String reason;

    public CustomerCancelOrderRequest(String reason) {
        this.reason = reason;
    }
}
