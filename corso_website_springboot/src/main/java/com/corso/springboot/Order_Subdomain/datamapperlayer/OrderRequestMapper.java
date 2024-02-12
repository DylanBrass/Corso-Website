package com.corso.springboot.Order_Subdomain.datamapperlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestCustomer;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "orderId")
    @Mapping(ignore = true, target = "orderTrackingNumber")
    @Mapping(ignore = true, target = "orderStatus")
    @Mapping(ignore = true, target = "serviceId")
    @Mapping(ignore = true, target = "orderDate")
    Order requestModelToOrder(OrderRequestExternal orderRequestExternal);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "orderId")
    @Mapping(ignore = true, target = "serviceId")
    @Mapping(ignore = true, target = "orderTrackingNumber")
    @Mapping(ignore = true, target = "orderStatus")
    @Mapping(ignore = true, target = "estimatedDuration")
    @Mapping(ignore = true, target = "hoursWorked")
    @Mapping(ignore = true, target = "userId")
    @Mapping(ignore = true, target = "orderDate")
    @Mapping(ignore = true, target = "customerEmail")
    @Mapping(ignore = true, target = "progressInformation")
    Order requestModelToOrderCustomer(OrderRequestCustomer orderRequestCustomer);
}
