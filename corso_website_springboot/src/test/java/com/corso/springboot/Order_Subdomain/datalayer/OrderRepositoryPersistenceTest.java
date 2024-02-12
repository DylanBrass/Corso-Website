package com.corso.springboot.Order_Subdomain.datalayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.logging.Logger;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryPersistenceTest {


    Logger logger = Logger.getLogger(OrderRepositoryPersistenceTest.class.getName());

    @Autowired
    OrderRepository orderRepository;

    private Order preSavedOrder;

    @BeforeEach
    public void setup(){
        orderRepository.deleteAll();
        preSavedOrder = orderRepository.save(
                new Order(new ServiceIdentifier("982577e6-1909-46b8-8583-e08c9daa4e9b"), "auth0|12548919","","2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St", "Montreal", "J5R 5J4", "",5,1)
        );

        logger.info("preSavedOrder: " + preSavedOrder.getOrderId().getOrderId());

    }


    @Test
    public void getOrderById_ShouldSucceed() {
        // Arrange
        String orderId = String.valueOf(preSavedOrder.getOrderId().getOrderId());

        logger.info("orderId: " + orderId);
        // Act
        Order result = orderRepository.findByOrderId_OrderId(orderId);

        // Assert
        assert result.getOrderId().getOrderId().equals(orderId);
    }

    @Test
    public void getAllUsersOrders_ShouldSucceed() {
        // Arrange
        String userId = preSavedOrder.getUserId();

        // Act
        List<Order> result = orderRepository.findAllByUserIdWithPagination(userId, 10,0);

        // Assert
        assert result.size() == 1;
    }

    @Test
    public void getCountByOrderStatus_ShouldSucceed() {
        // Arrange
        String status = "IN_PROGRESS";
        // Act
        int result = orderRepository.countByOrderStatus(status);

        // Assert
        assert result == 1;
    }


}