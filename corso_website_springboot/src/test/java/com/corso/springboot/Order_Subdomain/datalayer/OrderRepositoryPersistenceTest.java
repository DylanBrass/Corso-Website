package com.corso.springboot.Order_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class OrderRepositoryPersistenceTest {

    @Autowired
    OrderRepository orderRepository;

    private Order preSavedOrder;

    @BeforeEach
    public void setup(){
        orderRepository.deleteAll();
        preSavedOrder = orderRepository.save(
                new Order("982577e6-1909-46b8-8583-e08c9daa4e9b", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St")
        );
    }

//    @Test
//    public void whenFindAllByOrderStatusWithPagination_thenReturnAllOrdersWithStatus() {
//        //arrange
//        Integer expectedNumOrders = 1;
//
//        //act
//        List<Order> orders = orderRepository.findAllByOrderStatusWithPagination(Status.IN_PROGRESS.toString(), 50, 0);
//
//        //assert
//        assert orders.size() == expectedNumOrders;
//    }
}