package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class OrderResponse {
    public String orderId;

    public String orderTrackingNumber;

    public String serviceId;

    public ServiceResponse service;

    public String userId;

    public String progressInformation;

    public String orderDate;

    public String dueDate;

    public Status orderStatus;

    public String orderDescription;

    public String customerFullName;

    public String customerEmail;

    public String customerPhone;

    public String customerAddress;

    public String customerCity;

    public String customerPostalCode;

    public String customerApartmentNumber;


    public int totalOrdersMatchingRequest;

    public int hoursWorked;

    public int estimatedDuration;
}