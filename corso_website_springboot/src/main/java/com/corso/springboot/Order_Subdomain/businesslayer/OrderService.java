package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerCancelOrderRequest;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.presentationlayer.*;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrdersPerMonth.OrdersPerMonth;

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

    OrderResponse updateOrderStatusToCancelled(String orderId, DeclineOrderRequest declineOrderRequest) throws MessagingException;
    void cancelByCustomerOrderByOrderId(String orderId, String userId, CustomerCancelOrderRequest cancelOrderRequest) throws MessagingException;

    OrderResponse updateOrderByOrderId(String orderId, String userId, OrderCustomerRequest customerUpdateOrderRequest) throws MessagingException;

    OrderResponse updateOrderProgressionByOrderId(String orderId, OrderProgressionRequest orderProgressionRequest) throws MessagingException;


    Integer countAllOverdueOrders();

    List<OrdersPerMonth> getTotalOrderRequestByMonth(int year);

    OrderResponse CustomerRequestOrder(String userId, String userEmail, OrderRequestCustomer orderRequestCustomer) throws MessagingException;

    void deleteOrderByOrderId(String orderId);



    OrderResponse setCustomerOrderIdToCompletedStatus(String orderId) throws  MessagingException;
}