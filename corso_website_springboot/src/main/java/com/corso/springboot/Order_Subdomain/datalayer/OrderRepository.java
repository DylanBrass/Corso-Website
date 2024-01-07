package com.corso.springboot.Order_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByOrderStatusEquals(Status status);

    @Query(value = "SELECT * FROM orders WHERE order_status = :status ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllByOrderStatusWithPagination(@Param("status") String status, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT * FROM orders ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllOrders(int pageSize, int offset);

    Order findByOrderId_OrderId(String orderId);

    @Query(value = "SELECT * FROM orders WHERE due_date < CURRENT_DATE AND (order_status = 'IN_PROGRESS') ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllOverdueOrdersWithPagination(@Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT * FROM orders WHERE due_date < CURRENT_DATE AND (order_status = 'IN_PROGRESS') ORDER BY id", nativeQuery = true)
    List<Order> findAllOverdueOrders();
}
