package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.datalayer.*;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderRequestMapper;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceServiceImpl;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.configuration.security.exceptions.Auth0Error;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.corso.springboot.utils.exceptions.Orders.OrderNotFoundException;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderResponseMapper orderResponseMapper;

    private final OrderRequestMapper orderRequestMapper;

    private final ServiceService serviceService;

    private final Auth0ManagementService auth0ManagementService;

    @Override
    public List<OrderResponse> getAllOrdersByStatus(Map<String, String> queryParams, int pageSize, int offset) {

        if (queryParams.get("status") == null && pageSize == 0 && offset == 0) {
            return orderResponseMapper.toOrdersResponse(orderRepository.findAll());
        }

        if (queryParams.get("status") == null) {
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllOrders(pageSize, offset));
        }

        if (pageSize == 0 && offset == 0) {
            Status statusEnum = Status.valueOf(queryParams.get("status").toUpperCase());
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusEquals(statusEnum));
        }

        Status statusEnum = Status.valueOf(queryParams.get("status").toUpperCase());
        return orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusWithPagination(String.valueOf(statusEnum), pageSize, offset));
    }

    @Override
    public List<OrderResponse> getAllOverdueOrders(int pageSize, int offset) {
        if (pageSize == 0 && offset == 0) {
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllOverdueOrders());
        }

        return orderResponseMapper.toOrdersResponse(orderRepository.findAllOverdueOrdersWithPagination(pageSize, offset));
    }


    @Override
    public OrderResponse createOrderExternal(OrderRequestExternal orderRequestExternal) throws IOException, InterruptedException {

        if (serviceService.getServiceByServiceId(orderRequestExternal.getServiceId()) == null) {
            throw new ServiceNotFoundException("Service not found");
        }


        Order order = orderRequestMapper.requestModelToOrder(orderRequestExternal);

        if (orderRequestExternal.getUserId() == null || orderRequestExternal.getUserId().isEmpty())
            order.setUserId("external_client");
        else {
            UserInfoResponse user = null;
            try {
                user = auth0ManagementService.getUserInfo(orderRequestExternal.getUserId());
            } catch (NullPointerException | InterruptedException | Auth0Error e) {
                order.setUserId("external_client");
            }

            if (user == null) {
                order.setUserId("external_client");
            }
            order.setUserId(orderRequestExternal.getUserId());
        }

        order.setOrderId(new OrderIdentifier());

        order.setOrderTrackingNumber(new OrderTrackingNumber());

        if (orderRequestExternal.getDaysLeft() == 0) order.setDaysLeft(orderRequestExternal.getEstimatedDuration());
        else order.setDaysLeft(orderRequestExternal.getDaysLeft());

        order.setOrderStatus(Status.IN_PROGRESS);

        order.setServiceId(new ServiceIdentifier(orderRequestExternal.getServiceId()));

        order.setOrderDate(String.valueOf(LocalDate.now()));

        if (orderRequestExternal.getDueDate() == null || orderRequestExternal.getDueDate().isEmpty())
            order.setDueDate(null);
        else order.setDueDate(orderRequestExternal.getDueDate());

        try {
            return orderResponseMapper.toOrderResponse(orderRepository.save(order));
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public OrderResponse getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId_OrderId(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        serviceService.getServiceByServiceId(order.getServiceId().getServiceId());

        OrderResponse orderResponse = orderResponseMapper.toOrderResponse(order);

        orderResponse.setService(serviceService.getServiceByServiceId(order.getServiceId().getServiceId()));

        return orderResponse;
    }
}
