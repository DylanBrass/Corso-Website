package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Status;
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
    public String userId;
    public String orderDate;
    private String dueDate;
    public Status orderStatus;
    private String orderDescription;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
}