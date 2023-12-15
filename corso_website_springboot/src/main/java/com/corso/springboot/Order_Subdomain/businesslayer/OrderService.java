package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderResponse> getAllOrdersByStatus(Map<String, String> queryParams, int pageSize, int offset);

    OrderResponse createOrderExternal(OrderRequestExternal orderRequestExternal);
}