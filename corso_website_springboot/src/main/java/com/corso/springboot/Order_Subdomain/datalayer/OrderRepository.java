package com.corso.springboot.Order_Subdomain.datalayer;

import com.corso.springboot.Order_Subdomain.presentationlayer.OrderProgressionRequest;
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
    List<Order> findAllByUserIdAndOrderStatusWithPagination(@Param("userId") String userId, @Param("pageSize") int pageSize, @Param("offset") int offset, @Param("status") String status);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :userId AND order_status = :status", nativeQuery = true)
    int countByUserIdAndOrderStatus(String userId, String status);

    List<Order> findAllByUserIdAndOrderStatus(String userId, Status status);

    int countAllBy();

    boolean existsByUserId(String userId);


    @Query(value = """
            SELECT
                     months.n AS date_month,
                     IFNULL(y, :year) AS date_year,
                     IFNULL(total_orders, 0) AS total_orders
                 FROM (
                          SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
                          UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8
                          UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
                      ) AS months
                          LEFT JOIN (
                     SELECT
                         MONTH(orders.order_date) AS m,
                         YEAR(orders.order_date) AS y,
                         COUNT(orders.order_id) AS total_orders
                     FROM orders
                     WHERE YEAR(orders.order_date) = :year
                       AND orders.order_status NOT IN ('DECLINED')
                     GROUP BY m, y
                 ) AS report ON months.n = report.m
                 ORDER BY months.n;
    """, nativeQuery = true
    )
    List<Object[]> getTotalOrderRequestByMonth(@Param("year") int year);


}
