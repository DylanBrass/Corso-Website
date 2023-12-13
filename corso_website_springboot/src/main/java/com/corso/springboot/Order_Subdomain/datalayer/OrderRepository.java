package com.corso.springboot.Order_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByOrderStatusEquals(Status status);

    @Query(value = "SELECT * FROM orders WHERE order_status = :status ORDER BY order_id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllByOrderStatusWithPagination(@Param("status") String status, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT * FROM orders ORDER BY order_id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllServices(int pageSize, int offset);


}
