package com.corso.springboot.Order_Subdomain.businesslayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderServiceImpl;
import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.datalayer.OrderRepository;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderResponseMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
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

    @Test
    void getAllInProgressOrders_ShouldSucceed() {
        // Arrange
        Map<String, String> queryParams = Map.of("status", "in_progress");

        List<OrderResponse> mockOrderResponses = Arrays.asList(
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", "auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St"),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", "auth0|65702e81e9661e14ab3aac88", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St")
        );

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.IN_PROGRESS.toString(), 50, 0)).thenReturn(Arrays.asList(
                new Order("982577e6-1909-46b8-8583-e08c9daa4e9b", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St")
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
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f411", "T1YX4S", "982577e6-1909-46b8-8583-e08c9daa4e9b", "auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St"),
                new OrderResponse("ac5ca2b4-d53c-4516-8d4b-17665b46f415", "T1YX4W", "982577e6-1909-46b8-8583-e08c9daa4e9c", "auth0|65702e81e9661e14ab3aac88", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St")
        );

        Mockito.when(orderRepository.findAllByOrderStatusWithPagination(Status.PENDING.toString(), 50, 0)).thenReturn(Arrays.asList(
                new Order("982577e6-1909-46b8-8583-e08c9daa4e9b", "2023-01-01", "2023-02-01", Status.PENDING, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St")
        ));

        Mockito.when(orderResponseMapper.toOrdersResponse(Mockito.anyList())).thenReturn(mockOrderResponses);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersByStatus(queryParams, 50, 0);

        // Assert
        assertEquals(mockOrderResponses, result);
        assertEquals(Status.PENDING, result.get(0).getOrderStatus());
        assertEquals(Status.PENDING, result.get(1).getOrderStatus());
    }
}
