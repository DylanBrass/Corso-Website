package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerCancelOrderRequest;
import com.corso.springboot.Order_Subdomain.datalayer.*;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.AcceptOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.DeclineOrderRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestExternal;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.exceptions.Orders.OrderNotFoundException;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceImplUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderResponseMapper orderResponseMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private Auth0ManagementService auth0ManagementService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    ServiceService serviceService;

    @Test
    void getAllOrders_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of();

        List<OrderResponse> mockOrderResponses = new ArrayList<>();
        mockOrderResponses.add(new OrderResponse("orderId1", "trackingNumber1", "serviceId1", null, "userId1", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Description1", "Customer1", "customer1@example.com", "1234567890", "123 Main St", "City1", "PostalCode1", "ApartmentNumber1", 2, 1, 1));
        mockOrderResponses.add(new OrderResponse("orderId2", "trackingNumber2", "serviceId2", null, "userId2", "", "2023-01-02", "2023-02-02", Status.COMPLETED, "Description2", "Customer2", "customer2@example.com", "1234567890", "456 Main St", "City2", "PostalCode2", "ApartmentNumber2", 3, 1, 1));

        Mockito.when(orderRepository.count()).thenReturn(2L);
        Mockito.when(orderRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOrders(queryParams, 0, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(2, result.size());
        assertEquals(Status.IN_PROGRESS, result.get(0).getOrderStatus());
        assertEquals(Status.COMPLETED, result.get(1).getOrderStatus());
    }

    @Test
    void getAllOrdersByStatus_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of("status", "in_progress");

        List<OrderResponse> mockOrderResponses = Arrays.asList(new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 1, 2, 1),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1));

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.IN_PROGRESS.toString(), 50, 0)).thenReturn(Arrays.asList(new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersByStatus(queryParams, 50, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(Status.IN_PROGRESS, result.get(0).getOrderStatus());
        assertEquals(Status.IN_PROGRESS, result.get(1).getOrderStatus());
    }

    @Test
    void getAllInProgressOrders_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of("status", "in_progress");

        List<OrderResponse> mockOrderResponses = Arrays.asList(new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "",
                "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1), new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1));
        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.IN_PROGRESS.toString(), 50, 0)).thenReturn(List.of(new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersByStatus(queryParams, 50, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(Status.IN_PROGRESS, result.get(0).getOrderStatus());
        assertEquals(Status.IN_PROGRESS, result.get(1).getOrderStatus());
    }

    @Test
    void getALlPendingOrders_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of("status", "pending");

        List<OrderResponse> mockOrderResponses = Arrays.asList(new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1), new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1));

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.PENDING.toString(), 50, 0)).thenReturn(List.of(new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersByStatus(queryParams, 50, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(Status.PENDING, result.get(0).getOrderStatus());
        assertEquals(Status.PENDING, result.get(1).getOrderStatus());
    }

    @Test
    void getAllOverdueOrders_ShouldSucceed() {
        // Arrange
        List<OrderResponse> mockOrderResponses = Arrays.asList(new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1), new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom dev proj", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 2, 1, 1));

        Mockito.when(orderRepository.findAllOverdueOrdersWithPagination(50, 0)).thenReturn(Arrays.asList(new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOverdueOrders(50, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(Status.IN_PROGRESS, result.get(0).getOrderStatus());
        assertEquals(Status.IN_PROGRESS, result.get(1).getOrderStatus());
        assertEquals("Custom web development project", result.get(0).getOrderDescription());
        assertEquals("Custom dev proj", result.get(1).getOrderDescription());

    }

    @Test
    public void getOrderById_ShouldSucceed() {
        // Arrange

        Order order = new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1);

        String orderId = order.getOrderId().getOrderId();

        Mockito.when(serviceService.getServiceByServiceId("982577e6-1909-46b8-8583-e08c9daa4e9b")).thenReturn(new ServiceResponse("982577e6-1909-46b8-8583-e08c9daa4e9b", "Custom web development", "Custom web development project", "https://www.google.com", "https"));

        Mockito.when(orderResponseMapper.toOrderResponse(order)).thenReturn(new OrderResponse(orderId, order.getOrderTrackingNumber().getOrderTrackingNumber(), order.getServiceId().getServiceId(), null, order.getUserId(), order.getProgressInformation(), order.getOrderDate(), order.getDueDate(), order.getOrderStatus(), order.getOrderDescription(), order.getCustomerFullName(), order.getCustomerEmail(), order.getCustomerPhone(), order.getCustomerAddress(), order.getCustomerCity(), order.getCustomerPostalCode(), order.getCustomerApartmentNumber(), 2, 1, 1));
        Mockito.when(orderRepository.findByOrderId_OrderId(orderId)).thenReturn(order);

        // Act
        OrderResponse result = orderService.getOrderByOrderId(orderId);

        // Assert
        assertEquals(orderId, result.getOrderId());
    }

    @Test
    public void getCustomerOrders_ShouldSucceed() {
        // Arrange
        String userId = "auth0|12548919";
        int pageSize = 10;
        int offset = 0;

        List<OrderResponse> mockOrderResponses = Arrays.asList(new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "test@email.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 21, 1, 1));

        Mockito.when(orderRepository.countByUserId(userId)).thenReturn(1);

        Mockito.when(orderRepository.findAllByUserIdWithPagination(userId, pageSize, offset)).thenReturn(Arrays.asList(new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), userId, "", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "test@email.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        Mockito.when(serviceService.getServiceByServiceId("982577e6-1909-46b8-8583-e08c9daa4e9b")).thenReturn(new ServiceResponse("982577e6-1909-46b8-8583-e08c9daa4e9b", "Custom web development", "Custom web development project", "https://www.google.com", "https"));

        // Act

        List<OrderResponse> result = orderService.getCustomerOrders(userId, pageSize, offset);

        // Assert

        assertEquals(mockOrderResponses, result);


    }

    @Test
    void updateOrderStatusToAccepted_ShouldSucceed() throws MessagingException {
        // Arrange
        Order mockOrder = new Order();
        mockOrder.setOrderId(new OrderIdentifier());
        mockOrder.setOrderStatus(Status.PENDING);
        mockOrder.setOrderTrackingNumber(new OrderTrackingNumber());

        OrderResponse mockOrderResponse = new OrderResponse(mockOrder.getOrderId().getOrderId(),
                mockOrder.getOrderTrackingNumber().getOrderTrackingNumber(), "982577e6-1909-46b8-8583-e08c9daa4e9b", null,
                "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01",
                "2023-02-01", Status.PENDING, "Custom web development project",
                "John Doe", "john@example.com", "1234567890",
                "123 Main St", "Montreal", "J5R 5J4", "",
                1, 2, 1);


        Mockito.when(orderRepository.findByOrderId_OrderId(mockOrder.getOrderId().getOrderId())).thenReturn(mockOrder);
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        Mockito.when(orderResponseMapper.toOrderResponse(any(Order.class))).thenReturn(mockOrderResponse);


        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(200);

        AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest("John Doe");

        // Act
        OrderResponse result = orderService.updateOrderStatusToAccepted(mockOrder.getOrderId().getOrderId(), acceptOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(Status.IN_PROGRESS, mockOrder.getOrderStatus(), "Order status should be updated to IN_PROGRESS");
        verify(orderRepository).save(mockOrder);

    }

    @Test
    public void whenCreatingExternalOrderServiceIdNotFound_shouldThrowServiceNotFoundException()  {
        // Arrange
        when(serviceService.getServiceByServiceId("982577e6-1909-46b8-8583-e08c9daa4e9b")).thenReturn(null);

        // Act & Assert

        OrderRequestExternal orderRequestExternal = OrderRequestExternal.builder()
                .serviceId("982577e6-1909-46b8-8583-e08c9daa4e9b")
                .orderDescription("Custom web development project")
                .customerFullName("John Doe")
                .customerEmail("email@email.com")
                .customerPhone("1234567890")
                .customerAddress("123 Main St")
                .customerCity("Montreal")
                .customerPostalCode("J5R 5J4")
                .build();

        try {
            orderService.createOrderExternal(orderRequestExternal);
        } catch (Exception e) {
            assertEquals("Service not found", e.getMessage());
        }

    }

    @Test
    void updateOrderStatusToDeclined_ShouldSucceed() throws MessagingException {

        // Arrange
        Order mockOrder = new Order();
        mockOrder.setOrderId(new OrderIdentifier());
        mockOrder.setOrderStatus(Status.PENDING);
        mockOrder.setOrderTrackingNumber(new OrderTrackingNumber());

        OrderResponse mockOrderResponse = new OrderResponse(mockOrder.getOrderId().getOrderId(),
                "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null,
                "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01",
                "2023-02-01", Status.PENDING, "Custom web development project",
                "John Doe", "john@example.com", "1234567890",
                "123 Main St", "Montreal", "J5R 5J4", "",
                1, 2, 1);

        Mockito.when(orderRepository.findByOrderId_OrderId(mockOrder.getOrderId().getOrderId())).thenReturn(mockOrder);
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        Mockito.when(orderResponseMapper.toOrderResponse(any(Order.class))).thenReturn(mockOrderResponse);

        DeclineOrderRequest orderRequest = new DeclineOrderRequest("test","test");

        when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(200);

        // Act
        OrderResponse result = orderService.updateOrderStatusToDeclined(mockOrder.getOrderId().getOrderId(), orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(Status.DECLINED, mockOrder.getOrderStatus(), "Order status should be updated to Declined");
        verify(orderRepository).save(mockOrder);

    }

    @Test
    public void testCancelByCustomerOrderFailsToSendEmail() throws MessagingException {
        // Arrange
        Order mockOrder = new Order();
        mockOrder.setOrderId(new OrderIdentifier());
        mockOrder.setOrderStatus(Status.PENDING);
        mockOrder.setUserId("auth0|65702e81e9661e14ab3aac89");
        mockOrder.setCustomerFullName("John Doe");
        mockOrder.setOrderTrackingNumber(new OrderTrackingNumber());

        when(orderRepository.findByOrderId_OrderIdAndUserId(mockOrder.getOrderId().getOrderId(), mockOrder.getUserId())).thenReturn(mockOrder);

        List<UserInfoResponse> admins = new ArrayList<>();
        admins.add(new UserInfoResponse("auth0|65702e81e9661e14ab3aac89", "test@email.com",
                "Doe", new HashMap<>()));
        when(auth0ManagementService.getAllAdmins()).thenReturn(admins);


        CustomerCancelOrderRequest customerCancelOrderRequest = new CustomerCancelOrderRequest("test");

        when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(500);


    @Test
    void updateOrderStatusToCancelled_ShouldSucceed() throws MessagingException {

        // Arrange
        Order mockOrder = new Order();
        mockOrder.setOrderId(new OrderIdentifier());
        mockOrder.setOrderStatus(Status.IN_PROGRESS);
        mockOrder.setOrderTrackingNumber(new OrderTrackingNumber());

        OrderResponse mockOrderResponse = new OrderResponse(mockOrder.getOrderId().getOrderId(),
                "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null,
                "auth0|65702e81e9661e14ab3aac89", "", "2023-01-01",
                "2023-02-01", Status.IN_PROGRESS, "Custom web development project",
                "John Doe", "john@example.com", "1234567890",
                "123 Main St", "Montreal", "J5R 5J4", "",
                1, 2, 1);
        assertThrows(MessagingException.class, () -> orderService.cancelByCustomerOrderByOrderId(mockOrder.getOrderId().getOrderId(), mockOrder.getUserId(), customerCancelOrderRequest));

    }

        Mockito.when(orderRepository.findByOrderId_OrderId(mockOrder.getOrderId().getOrderId())).thenReturn(mockOrder);
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        Mockito.when(orderResponseMapper.toOrderResponse(any(Order.class))).thenReturn(mockOrderResponse);

        DeclineOrderRequest orderRequest = new DeclineOrderRequest("test", "test");

        when(emailService.sendEmail(any(String.class), any(String.class), any(String.class), any())).thenReturn(200);

        // Act
        OrderResponse result = orderService.updateOrderStatusToCancelled(mockOrder.getOrderId().getOrderId(), orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(Status.CANCELLED, mockOrder.getOrderStatus(), "Order status should be updated to Cancelled");
        verify(orderRepository).save(mockOrder);
    }

}
