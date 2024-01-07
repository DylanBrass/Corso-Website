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

    public String orderDate;

    public String dueDate;

    public Status orderStatus;

    public String orderDescription;

    private String customerFullName;

    private String customerEmail;

    private String customerPhone;

    private String customerAddress;

    private String customerCity;

    private String customerPostalCode;

    private String customerApartmentNumber;

    private int totalOrdersMatchingRequest;
}