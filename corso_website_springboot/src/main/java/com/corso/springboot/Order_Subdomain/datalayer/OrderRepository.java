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


    @Query(value = "SELECT * FROM orders WHERE due_date < CURRENT_DATE AND (order_status = 'IN_PROGRESS') ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllOverdueOrdersWithPagination(@Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT * FROM orders WHERE due_date < CURRENT_DATE AND (order_status = 'IN_PROGRESS') ORDER BY id", nativeQuery = true)
    List<Order> findAllOverdueOrders();

    Order findByOrderId_OrderId(String orderId);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllByUserIdWithPagination(@Param("userId") String userId, @Param("pageSize") int pageSize, @Param("offset") int offset);

    int countByUserId(String userId);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE order_status = :status", nativeQuery = true)
    int countByOrderStatus(@Param("status") String status);


    @Query(value = "SELECT COUNT(*) FROM orders WHERE due_date < CURRENT_DATE AND (order_status = 'IN_PROGRESS')", nativeQuery = true)
    int countOverdueOrders();

    Order findByOrderId_OrderIdAndUserId(String orderId, String userId);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND order_status = :status ORDER BY id DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Order> findAllByUserIdAndOrderStatusWithPagination(@Param("userId") String userId, @Param("pageSize") int pageSize, @Param("offset") int offset,@Param("status") String status);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :userId AND order_status = :status", nativeQuery = true)
    int countByUserIdAndOrderStatus(String userId, String status);

    List<Order> findAllByUserIdAndOrderStatus(String userId, Status status);

    int countAllBy();

}
