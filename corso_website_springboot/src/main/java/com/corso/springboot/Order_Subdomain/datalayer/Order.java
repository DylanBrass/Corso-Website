package com.corso.springboot.Order_Subdomain.datalayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.utils.annotations.ValidCanadianPostalCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@Builder
@Table(name = "orders")
@AllArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Embedded
    private OrderIdentifier orderId;

    @Embedded
    private OrderTrackingNumber orderTrackingNumber;

    @Embedded
    private ServiceIdentifier serviceId;

    private String userId;

    private String orderDate;

    private String dueDate;

    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    private String orderDescription;

    private String customerFullName;

    private String customerEmail;

    private String customerPhone;

    private String customerAddress;

    private String customerCity;

    String customerPostalCode;

    String customerApartmentNumber;

    int estimatedDuration;

    int daysLeft;

    public Order(String userId, String orderDate, String dueDate, Status orderStatus, String orderDescription, String customerFullName, String customerEmail, String customerPhone, String customerAddress) {
        this.orderId = new OrderIdentifier();
        this.orderTrackingNumber = new OrderTrackingNumber();
        this.serviceId = new ServiceIdentifier();
        this.userId = userId;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.orderStatus = orderStatus;
        this.orderDescription = orderDescription;
        this.customerFullName = customerFullName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
    }
}