package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderServiceImpl;
import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.datalayer.OrderRepository;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.datalayer.Service;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceImplUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderResponseMapper orderResponseMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    ServiceService serviceService;

    @Test
    void getAllInProgressOrders_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of("status", "in_progress");

        List<OrderResponse> mockOrderResponses = Arrays.asList(
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2)
        );

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.IN_PROGRESS.toString(), 50, 0)).thenReturn(Arrays.asList(
                new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)
        ));

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

        List<OrderResponse> mockOrderResponses = Arrays.asList(
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2)
        );

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.PENDING.toString(), 50, 0)).thenReturn(Arrays.asList(
                new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)
        ));

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
        List<OrderResponse> mockOrderResponses = Arrays.asList(
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", null, "auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", null, "auth0|65702e81e9661e14ab3aac88", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom dev proj", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",2)
        );

        Mockito.when(orderRepository.findAllOverdueOrdersWithPagination(50, 0)).thenReturn(Arrays.asList(
                new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1)
        ));

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

        Order order = new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "", 5, 1);

        String orderId = order.getOrderId().getOrderId();

        Mockito.when(serviceService.getServiceByServiceId("982577e6-1909-46b8-8583-e08c9daa4e9b")).thenReturn(
                new ServiceResponse("982577e6-1909-46b8-8583-e08c9daa4e9b", "Custom web development", "Custom web development project", "https://www.google.com", "https")
                );

        Mockito.when(orderResponseMapper.toOrderResponse(order)).thenReturn(
                new OrderResponse(orderId, order.getOrderTrackingNumber().getOrderTrackingNumber(), order.getServiceId().getServiceId(), null, order.getUserId(), order.getOrderDate(), order.getDueDate(), order.getOrderStatus(), order.getOrderDescription(), order.getCustomerFullName(), order.getCustomerEmail(), order.getCustomerPhone(), order.getCustomerAddress(), order.getCustomerCity(), order.getCustomerPostalCode(), order.getCustomerApartmentNumber(),2)
        );
        Mockito.when(orderRepository.findByOrderId_OrderId(orderId)).thenReturn(
                order);

        // Act
        OrderResponse result = orderService.getOrderByOrderId(orderId);

        // Assert
        assertEquals(orderId, result.getOrderId());
    }
}
