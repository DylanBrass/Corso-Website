package com.corso.springboot.Order_Subdomain.datamapperlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
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


}
