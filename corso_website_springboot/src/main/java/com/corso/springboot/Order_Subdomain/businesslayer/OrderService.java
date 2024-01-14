package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.presentationlayer.AcceptOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.DeclineOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderResponse> getAllOrders(Map<String, String> queryParams, int pageSize, int offset);
    List<OrderResponse> getAllOrdersByStatus(Map<String, String> queryParams, int pageSize, int offset);

    List<OrderResponse> getAllOverdueOrders(int pageSize, int offset);

    OrderResponse createOrderExternal(OrderRequestExternal orderRequestExternal) throws IOException, InterruptedException;

    OrderResponse getOrderByOrderId(String orderId);

    List<OrderResponse> getCustomerOrders(String userId, int pageSize, int offset);

    OrderResponse getOrderByIdAndUserId(String orderId, String userId);

    OrderResponse updateOrderStatusToAccepted(String orderId, AcceptOrderRequest acceptOrderRequest) throws MessagingException;

    int countByOrderStatus(String status);

    List<OrderResponse> getCustomerCurrentOrders(String userId, Status status);

    int countAllOrders();

    OrderResponse updateOrderStatusToDeclined(String orderId, DeclineOrderRequest declineOrderRequest) throws MessagingException;

    boolean findAnyUserOrders(String userId);
}