package com.corso.springboot.Customer_Subdomain.presentationlayer;

import com.corso.springboot.utils.annotations.ValidCanadianPostalCode;
import com.corso.springboot.utils.annotations.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequest {

    @Email
    @NotBlank(message = "Email is required.")
    String email;

    String name;

    @ValidPhoneNumber
    String phone;

    @NotBlank(message = "Address is required.")
    String address;

    @NotBlank
    @ValidCanadianPostalCode
    String postalCode;

    @NotBlank(message = "City is required.")
    String city;

    String apartmentNumber;

}
