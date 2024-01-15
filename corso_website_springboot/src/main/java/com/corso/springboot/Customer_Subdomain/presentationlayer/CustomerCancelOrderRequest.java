package com.corso.springboot.Customer_Subdomain.presentationlayer;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
public class CustomerCancelOrderRequest {

    public String reason;

    @JsonCreator
    public CustomerCancelOrderRequest(@JsonProperty("reason") String reason) {
        this.reason = reason;
    }
}
