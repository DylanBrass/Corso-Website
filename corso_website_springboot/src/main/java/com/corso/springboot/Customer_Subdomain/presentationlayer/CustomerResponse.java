package com.corso.springboot.Customer_Subdomain.presentationlayer;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerResponse {

    String userId;

    String email;

    String name;

    String phone;

    String address;

    String postalCode;

    String city;

    String apartmentNumber;

    boolean verified;
}
