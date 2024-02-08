package com.corso.springboot.Order_Subdomain.presentationlayer;


import com.corso.springboot.utils.annotations.ValidCanadianPostalCode;
import com.corso.springboot.utils.annotations.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Builder
public class OrderRequestExternal {

    @NotBlank(message = "Service Id is required.")
    public String serviceId;

    public String userId;

    private String progressInformation;

    private String dueDate;

    private String orderDescription;

    @NotBlank(message = "Full Name is required.")
    private String customerFullName;

    @NotBlank(message = "Email is required.")
    @Email
    private String customerEmail;

    @ValidPhoneNumber
    private String customerPhone;

    @NotBlank(message = "Address is required.")
    private String customerAddress;

    @NotBlank(message = "City is required.")
    private String customerCity;

    @NotBlank(message = "Postal Code is required.")
    @ValidCanadianPostalCode
    String customerPostalCode;

    String customerApartmentNumber;

    int estimatedDuration;

    int hoursWorked;


}
