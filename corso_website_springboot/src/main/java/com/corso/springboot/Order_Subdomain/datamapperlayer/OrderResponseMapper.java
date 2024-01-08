package com.corso.springboot.Order_Subdomain.datamapperlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper {

    @Mapping(expression="java(order.getOrderId().getOrderId())", target = "orderId")
    @Mapping(expression="java(order.getServiceId().getServiceId())", target = "serviceId")
    @Mapping(expression="java(order.getOrderTrackingNumber().getOrderTrackingNumber())", target = "orderTrackingNumber")
    @Mapping(ignore = true, target = "service")
    @Mapping(ignore = true, target = "totalOrdersMatchingRequest")
    OrderResponse toOrderResponse(Order order);
    List<OrderResponse> toOrdersResponse(List<Order> orders);

}