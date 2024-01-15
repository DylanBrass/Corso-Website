package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerCancelOrderRequest;
import com.corso.springboot.Order_Subdomain.datalayer.*;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderRequestMapper;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.AcceptOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.DeclineOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.configuration.security.exceptions.Auth0Error;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.corso.springboot.utils.exceptions.Orders.OrderNotFoundException;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderResponseMapper orderResponseMapper;

    private final OrderRequestMapper orderRequestMapper;

    private final ServiceService serviceService;

    private final Auth0ManagementService auth0ManagementService;

    private final EmailService emailService;

    @Override
    public List<OrderResponse> getAllOrders(Map<String, String> queryParams, int pageSize, int offset) {
        String status = queryParams.get("status");
        String userId = queryParams.get("userId");

        if (status != null) status = status.toUpperCase();

        if (status == null && (userId == null || userId.isEmpty()) && pageSize == 0 && offset == 0) {
            return getAllOrdersWithDefaultSettings();
        }

        if (status == null && (userId == null || userId.isEmpty())) {
            return getAllOrdersWithPagination(pageSize, offset);
        }

        if (status == null) {
            return getCustomerOrders(userId, pageSize, offset);
        }

        if ((userId == null || userId.isEmpty())) {
            return getAllOrdersByStatus(queryParams, pageSize, offset);
        }

        return getOrderByCustomerAndStatus(userId, status, pageSize, offset);
    }


    public List<OrderResponse> getOrderByCustomerAndStatus(String userId, String status, int pageSize, int offset) {
        List<OrderResponse> responses = orderResponseMapper.toOrdersResponse(orderRepository.findAllByUserIdAndOrderStatusWithPagination(userId, pageSize, offset, status));

        int totalOrders = orderRepository.countByUserIdAndOrderStatus(userId, status);
        for (OrderResponse response : responses) {
            response.setTotalOrdersMatchingRequest(totalOrders);
            response.setService(serviceService.getServiceByServiceId(response.getServiceId()));
        }
        return responses;

    }

    private List<OrderResponse> getAllOrdersWithDefaultSettings() {
        int totalOrders = (int) orderRepository.count();
        List<OrderResponse> orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAll());

        orderResponses.forEach(orderResponse -> setOrderResponseDetails(orderResponse, totalOrders));

        return orderResponses;
    }

    private List<OrderResponse> getAllOrdersWithPagination(int pageSize, int offset) {
        int totalOrders = (int) orderRepository.count();
        List<OrderResponse> orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllOrders(pageSize, offset));

        orderResponses.forEach(orderResponse -> setOrderResponseDetails(orderResponse, totalOrders));

        return orderResponses;
    }

    private void setOrderResponseDetails(OrderResponse orderResponse, int totalOrders) {
        orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
        orderResponse.setTotalOrdersMatchingRequest(totalOrders);
    }

    @Override
    public List<OrderResponse> getAllOrdersByStatus(Map<String, String> queryParams, int pageSize, int offset) {

        if (queryParams.get("status") == null && pageSize == 0 && offset == 0) {
            return orderResponseMapper.toOrdersResponse(orderRepository.findAll());
        }

        if (queryParams.get("status") == null) {
            return orderResponseMapper.toOrdersResponse(orderRepository.findAllOrders(pageSize, offset));
        }

        int totalOrders = orderRepository.countByOrderStatus(queryParams.get("status").toUpperCase());
        Status statusEnum = Status.valueOf(queryParams.get("status").toUpperCase());

        List<OrderResponse> orderResponses;
        if (pageSize == 0 && offset == 0) {
            orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusEquals(statusEnum));

            for (OrderResponse orderResponse : orderResponses) {
                orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
                orderResponse.setTotalOrdersMatchingRequest(totalOrders);
            }

            return orderResponses;
        }

        orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllByOrderStatusWithPagination(String.valueOf(statusEnum), pageSize, offset));

        for (OrderResponse orderResponse : orderResponses) {
            orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
            orderResponse.setTotalOrdersMatchingRequest(totalOrders);
        }

        return orderResponses;
    }

    @Override
    public List<OrderResponse> getAllOverdueOrders(int pageSize, int offset) {

        int totalOrders = orderRepository.countOverdueOrders();

        List<OrderResponse> orderResponses;

        if (pageSize == 0 && offset == 0) {
            orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllOverdueOrders());

            for (OrderResponse orderResponse : orderResponses) {
                orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
                orderResponse.setTotalOrdersMatchingRequest(totalOrders);
            }

            return orderResponses;
        }

        orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllOverdueOrdersWithPagination(pageSize, offset));

        for (OrderResponse orderResponse : orderResponses) {
            orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
            orderResponse.setTotalOrdersMatchingRequest(totalOrders);
        }

        return orderResponses;
    }


    @Override
    public OrderResponse createOrderExternal(OrderRequestExternal orderRequestExternal) throws IOException {

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
        return getOrderResponse(orderId, order);
    }

    @Override
    public List<OrderResponse> getCustomerOrders(String userId, int pageSize, int offset) {
        List<OrderResponse> orderResponses;

        int totalOrders = orderRepository.countByUserId(userId);

        if (totalOrders == 0) {
            throw new OrderNotFoundException("No orders found for user: " + userId);
        }

        if (pageSize == 0 && offset == 0) {
            orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllByUserIdWithPagination(userId, 10, 0));
            for (OrderResponse orderResponse : orderResponses) {
                orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
                orderResponse.setTotalOrdersMatchingRequest(totalOrders);
            }

            return orderResponses;
        }

        orderResponses = orderResponseMapper.toOrdersResponse(orderRepository.findAllByUserIdWithPagination(userId, pageSize, offset));

        for (OrderResponse orderResponse : orderResponses) {
            orderResponse.setService(serviceService.getServiceByServiceId(orderResponse.getServiceId()));
            orderResponse.setTotalOrdersMatchingRequest(totalOrders);
        }

        return orderResponses;
    }

    @Override
    public OrderResponse getOrderByIdAndUserId(String orderId, String userId) {
        Order order = orderRepository.findByOrderId_OrderIdAndUserId(orderId, userId);
        return getOrderResponse(orderId, order);
    }

    @Override
    public List<OrderResponse> getCustomerCurrentOrders(String userId, Status status) {
        return orderResponseMapper.toOrdersResponse(orderRepository.findAllByUserIdAndOrderStatus(userId, status));
    }

    @Override
    public int countAllOrders() {
        return orderRepository.countAllBy();
    }


    private OrderResponse getOrderResponse(String orderId, Order order) {
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        OrderResponse orderResponse = orderResponseMapper.toOrderResponse(order);

        orderResponse.setService(serviceService.getServiceByServiceId(order.getServiceId().getServiceId()));

        return orderResponse;
    }

    public OrderResponse updateOrderStatusToAccepted(String orderId, AcceptOrderRequest acceptOrderRequest) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderId(orderId);
        Status newStatus = Status.IN_PROGRESS;
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
        String orderTrackingNumber = order.getOrderTrackingNumber().getOrderTrackingNumber();


        String subject = "Order #" + orderTrackingNumber + " has been accepted!";

        Map<String, String> parameters = new HashMap<>();

        int status = emailService.sendEmail(acceptOrderRequest.getRecipient(), subject, "ApproveOrder", parameters);

        if (status != 200) {
            throw new MessagingException("Failed to send email");
        }

        order.setOrderStatus(Status.valueOf(newStatus.toString()));
        order.setOrderDate(String.valueOf(LocalDate.now()));
        order.setDueDate(String.valueOf(LocalDate.now().plusDays(order.getDaysLeft())));

        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public int countByOrderStatus(String status) {
        return orderRepository.countByOrderStatus(status);
    }

    @Override
    public OrderResponse updateOrderStatusToDeclined(String orderId, DeclineOrderRequest declineOrderRequest) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderId(orderId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        Status newStatus = Status.DECLINED;

        String orderTrackingNumber = order.getOrderTrackingNumber().getOrderTrackingNumber();


        Map<String, String> parameters = new HashMap<>();
        log.info("Sending email to {}", declineOrderRequest.getRecipient());
        log.info("Reason email to {}", declineOrderRequest.getReasonForDecline());
        parameters.put("reasonForCancellation", declineOrderRequest.getReasonForDecline());

        String subject = "Order #" + orderTrackingNumber + " has been denied";

        int status = emailService.sendEmail(declineOrderRequest.getRecipient(), subject, "DeclineOrder", parameters);

        if (status != 200) {
            throw new MessagingException("Failed to send email");
        }

        order.setOrderStatus(Status.valueOf(newStatus.toString()));

        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public boolean findAnyUserOrders(String userId) {
        return orderRepository.existsByUserId(userId);
    }


    @Override
    public void cancelByCustomerOrderByOrderId(String orderId, String userId, CustomerCancelOrderRequest cancelOrderRequest) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderIdAndUserId(orderId, userId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }


        if (order.getOrderStatus() != Status.PENDING) {
            throw new InvalidRequestException("Order cannot be cancelled, it is not in pending status, meaning it was accepted or cancelled already!");
        }

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("reasonForCancellation", cancelOrderRequest.getReason());

        parameters.put("name", order.getCustomerFullName());

        List<UserInfoResponse> admins = auth0ManagementService.getAllAdmins();


        ExecutorService executorService = Executors.newFixedThreadPool(admins.size());
        AtomicInteger emailSent = new AtomicInteger();
        try {
            for (UserInfoResponse admin : admins) {
                executorService.submit(() -> {
                    try {
                        int emailStatus = emailService.sendEmail(admin.getEmail(),
                                "Order #" + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been cancelled",
                                "CustomerCancelOrder", parameters);

                        if (emailStatus != 200) {
                            log.error("Failed to send email to " + admin.getEmail());
                            throw new MessagingException("Failed to send email to one of the admins");
                        }

                        emailSent.getAndIncrement();
                    } catch (Exception e) {
                        log.error("Exception while sending email to " + admin.getEmail(), e);
                    }
                });
            }
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (emailSent.get() != admins.size()) {
            throw new MessagingException("Failed to notify our electricians, please try again later");
        }

        order.setOrderStatus(Status.CANCELLED);

        orderRepository.save(order);
    }


    @Override
    public OrderResponse updateOrderStatusToCancelled(String orderId, DeclineOrderRequest declineOrderRequest) throws MessagingException {

        Order order = orderRepository.findByOrderId_OrderId(orderId);
        Status newStatus = Status.CANCELLED;
        String orderTrackingNumber = order.getOrderTrackingNumber().getOrderTrackingNumber();


        Map<String, String> parameters = new HashMap<>();
        log.info("Sending email to {}", declineOrderRequest.getRecipient());
        log.info("Reason email to {}", declineOrderRequest.getReasonForDecline());
        parameters.put("reasonForCancellation", declineOrderRequest.getReasonForDecline());

        String subject = "Order #" + orderTrackingNumber + " has been canceled";

        int status = emailService.sendEmail(declineOrderRequest.getRecipient(), subject, "CancelOrder", parameters);

        if (status != 200) {
            throw new MessagingException("Failed to send email");
        }

        order.setOrderStatus(Status.valueOf(newStatus.toString()));

        return orderResponseMapper.toOrderResponse(orderRepository.save(order));

    }
}
