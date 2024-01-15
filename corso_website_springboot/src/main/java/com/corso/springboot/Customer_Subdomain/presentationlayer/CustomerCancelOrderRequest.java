package com.corso.springboot.Customer_Subdomain.presentationlayer;


import lombok.*;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerCancelOrderRequest {

    public String reason;

}
