package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.datalayer.CustomerRepository;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerCancelOrderRequest;
import com.corso.springboot.Order_Subdomain.datalayer.*;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderRequestMapper;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.*;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrdersPerMonth.OrdersPerMonth;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.configuration.security.exceptions.Auth0Error;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.Tools.BasicTools;
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

    private final BasicTools basicTools;

    private final CustomerRepository customerRepository;

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

        if (orderRequestExternal.getHoursWorked() > orderRequestExternal.getEstimatedDuration()) {
            throw new InvalidRequestException("Hours worked cannot be greater than estimated duration");
        }

        if (orderRequestExternal.getHoursWorked() < 0) {
            throw new InvalidRequestException("Hours worked cannot be less than 0");
        }

        order.setOrderId(new OrderIdentifier());

        order.setOrderTrackingNumber(new OrderTrackingNumber());

        order.setHoursWorked(orderRequestExternal.getHoursWorked());

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
        order.setDueDate(order.getDueDate());

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

        // add func
        basicTools.sendEmailToAdmins("Order #" + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been cancelled", "CustomerCancelOrder", parameters);

        order.setOrderStatus(Status.CANCELLED);

        orderRepository.save(order);
    }

    @Override
    public OrderResponse updateOrderByOrderId(String orderId, String userId, OrderCustomerRequest customerUpdateOrderRequest) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderIdAndUserId(orderId, userId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        if (order.getOrderStatus() != Status.PENDING) {
            throw new InvalidRequestException("Order cannot be updated, it is not in pending status, meaning it was accepted or cancelled already!");
        }

        Map<String, String> parameters = getStringStringMap(customerUpdateOrderRequest, order);

        basicTools.sendEmailToAdmins("Order #" + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been updated", "UpdateOrder", parameters);

        order.setCustomerAddress(customerUpdateOrderRequest.getCustomerAddress());

        order.setCustomerPhone(customerUpdateOrderRequest.getCustomerPhone());

        order.setCustomerCity(customerUpdateOrderRequest.getCustomerCity());

        order.setCustomerPostalCode(customerUpdateOrderRequest.getCustomerPostalCode());

        order.setCustomerApartmentNumber(customerUpdateOrderRequest.getCustomerApartmentNumber());


        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse updateOrderProgressionByOrderId(String orderId, OrderProgressionRequest orderProgressionRequest) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderId(orderId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        if (order.getOrderStatus() != Status.IN_PROGRESS) {
            throw new InvalidRequestException("Order cannot be updated, it is not in in progress status, meaning it was accepted or cancelled already : " + order.getOrderStatus());
        }

        Map<String, String> parameters = getStringStringMapForOrderProgression(orderProgressionRequest, order);

        emailService.sendEmail(order.getCustomerEmail(), "Order #" + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been updated", "UpdateOrder", parameters);


        order.setProgressInformation(orderProgressionRequest.getProgression());

        order.setHoursWorked(orderProgressionRequest.getHoursWorked());

        order.setEstimatedDuration(orderProgressionRequest.getEstimatedDuration());

        order.setServiceId(new ServiceIdentifier(orderProgressionRequest.getServiceId()));

        OrderResponse orderResponse = orderResponseMapper.toOrderResponse(orderRepository.save(order));

        ServiceResponse service = serviceService.getServiceByServiceId(orderProgressionRequest.getServiceId());

        if (service == null)
            throw new ServiceNotFoundException("Service not found with id: " + orderProgressionRequest.getServiceId());



        orderResponse.setService(service);

        return orderResponse;

    }

    private static Map<String, String> getStringStringMapForOrderProgression(OrderProgressionRequest orderProgressionRequest, Order order) {
        Map<String, String> parameters = new HashMap<>();

        //Previous order details
        parameters.put("name", order.getCustomerFullName());

        parameters.put("orderId", order.getOrderTrackingNumber().getOrderTrackingNumber());

        parameters.put("previousAddress", order.getCustomerAddress());

        parameters.put("previousPhoneNumber", order.getCustomerPhone());

        parameters.put("previousCity", order.getCustomerCity());

        parameters.put("previousPostalCode", order.getCustomerPostalCode());

        parameters.put("previousApartmentNumber", order.getCustomerApartmentNumber());

        parameters.put("previousProgressionOrder", order.getProgressInformation());

        parameters.put("previousHoursWorked", String.valueOf(order.getHoursWorked()));

        parameters.put("previousEstimatedDuration", String.valueOf(order.getEstimatedDuration()));




        //New order details

        parameters.put("name", order.getCustomerFullName());

        parameters.put("orderId", order.getOrderTrackingNumber().getOrderTrackingNumber());

        parameters.put("newAddress", order.getCustomerAddress());

        parameters.put("newPhoneNumber", order.getCustomerPhone());

        parameters.put("newCity", order.getCustomerCity());

        parameters.put("newPostalCode", order.getCustomerPostalCode());

        parameters.put("newApartmentNumber", order.getCustomerApartmentNumber());

        parameters.put("newProgressionOrder", orderProgressionRequest.getProgression());

        parameters.put("newHoursWorked", String.valueOf(orderProgressionRequest.getHoursWorked()));

        parameters.put("newEstimatedDuration", String.valueOf(orderProgressionRequest.getEstimatedDuration()));

        return parameters;
    }


    @Override
    public Integer countAllOverdueOrders() {
        return orderRepository.countOverdueOrders();
    }

    @Override
    public List<OrdersPerMonth> getTotalOrderRequestByMonth(int year) {
        return orderRepository.getTotalOrderRequestByMonth(year).stream()
                .map(OrdersPerMonth::new)
                .toList();
    }

    @Override
    public void deleteOrderByOrderId(String orderId) {

        Order order = orderRepository.findByOrderId_OrderId(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        if (order.getOrderStatus() != Status.CANCELLED
                && order.getOrderStatus() != Status.DECLINED
                && order.getOrderStatus() != Status.COMPLETED
        ) {
            throw new InvalidRequestException("Order cannot be deleted, it is not in cancelled, declined or completed status! : " + order.getOrderStatus().toString());
        }




        orderRepository.delete(order);

    }


    @Override
        public OrderResponse setCustomerOrderIdToCompletedStatus(String orderId) throws MessagingException {
        Order order = orderRepository.findByOrderId_OrderId(orderId);

        if (order == null) {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }

        if (order.getOrderStatus() != Status.IN_PROGRESS || order.getHoursWorked() == 0) {
            throw new InvalidRequestException("Order cannot be updated, it is not in in progress status, meaning it was accepted or cancelled already.");
        }


        Status newcompletedStatus = Status.COMPLETED;

        String recipient = order.getCustomerEmail();
        String subject = "Order #" + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been completed";
        String text = "Dear " + order.getCustomerFullName() + ",\n\n" +
                "We are pleased to inform you that your order with ID: " + order.getOrderTrackingNumber().getOrderTrackingNumber() + " has been completed.\n\n" +
                "Thank you for choosing us.\n\n" +
                "Sincerely,\n" +
                "Corso Electric Inc.  " +
                "\n" +
                "\n" +
                "\n" +
                "\n"+
                "Cher(e) " + order.getCustomerFullName() + ",\n\n" +
                "Nous sommes heureux de vous informer que votre commande avec l'ID: " + order.getOrderTrackingNumber().getOrderTrackingNumber()  + " a été complétée.\n\n" +
                "Merci de nous avoir choisi.\n\n" +
                "Sincèrement,\n" +
                "Corso Electric Inc.";




        emailService.sendEmail(recipient, subject, text);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", order.getCustomerFullName());
        parameters.put("orderId", order.getOrderTrackingNumber().getOrderTrackingNumber());
        parameters.put("previousAddress", order.getCustomerAddress());
        parameters.put("previousPhone", order.getCustomerPhone());
        parameters.put("previousCity", order.getCustomerCity());
        parameters.put("previousPostalCode", order.getCustomerPostalCode());
        parameters.put("previousApartmentNumber", order.getCustomerApartmentNumber());
        parameters.put("newAddress", order.getCustomerAddress());
        parameters.put("newPhone", order.getCustomerPhone());
        parameters.put("newCity", order.getCustomerCity());
        parameters.put("newPostalCode", order.getCustomerPostalCode());
        parameters.put("newApartmentNumber", order.getCustomerApartmentNumber());
        parameters.put("progression", order.getProgressInformation());
        parameters.put("hoursWorked", String.valueOf(order.getHoursWorked()));
        parameters.put("estimatedDuration", String.valueOf(order.getEstimatedDuration()));
        parameters.put("serviceId", order.getServiceId().getServiceId());
        parameters.put("reasonForCancellation", "Order has been completed");

        order.setOrderStatus(newcompletedStatus);


        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }


    private static Map<String, String> getStringStringMap(OrderCustomerRequest customerUpdateOrderRequest, Order order) {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("name", order.getCustomerFullName());

        parameters.put("orderId", order.getOrderTrackingNumber().getOrderTrackingNumber());

        parameters.put("previousAddress", order.getCustomerAddress());

        parameters.put("previousPhone", order.getCustomerPhone());


        parameters.put("previousCity", order.getCustomerCity());

        parameters.put("previousPostalCode", order.getCustomerPostalCode());

        parameters.put("previousApartmentNumber", order.getCustomerApartmentNumber());

        parameters.put("newAddress", customerUpdateOrderRequest.getCustomerAddress());

        parameters.put("newPhone", customerUpdateOrderRequest.getCustomerPhone());

        parameters.put("newCity", customerUpdateOrderRequest.getCustomerCity());

        parameters.put("newPostalCode", customerUpdateOrderRequest.getCustomerPostalCode());

        parameters.put("newApartmentNumber", customerUpdateOrderRequest.getCustomerApartmentNumber());
        return parameters;
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

    @Override
    public OrderResponse CustomerRequestOrder(String userId, String userEmail, OrderRequestCustomer orderRequestCustomer) throws MessagingException{

        if (serviceService.getServiceByServiceId(orderRequestCustomer.getServiceId()) == null) {
            throw new ServiceNotFoundException("Service not found");
        }

        Order order = orderRequestMapper.requestModelToOrderCustomer(orderRequestCustomer);

        // Check if customer exists in our db -> if not, use email from token
        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null || !customer.isVerified())
            order.setCustomerEmail(userEmail);
        else {
            order.setCustomerEmail(customer.getEmail());
        }

        order.setUserId(userId);
        order.setOrderId(new OrderIdentifier());
        order.setOrderTrackingNumber(new OrderTrackingNumber());
        order.setOrderStatus(Status.PENDING);
        order.setServiceId(new ServiceIdentifier(orderRequestCustomer.getServiceId()));
        order.setOrderDate(String.valueOf(LocalDate.now()));

        // sending email to the user
        String subject = "Order Request Confirmation";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", order.getCustomerFullName());
        parameters.put("orderId", order.getOrderTrackingNumber().getOrderTrackingNumber());
        int status = emailService.sendEmail(order.getCustomerEmail(), subject, "OrderRequestConfirmation", parameters);

        if (status != 200) {
            throw new MessagingException("Failed to send email <3");
        }

        //sending email to admins
        basicTools.sendEmailToAdmins("An Order Has Been Requested", "OrderRequestConfirmationAdmins", parameters);

        return orderResponseMapper.toOrderResponse(orderRepository.save(order));
    }

}
