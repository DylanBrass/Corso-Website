package com.corso.springboot.Service_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {
    ServiceEntity findByServiceIdentifier_ServiceId(String serviceId);
    List<ServiceEntity> findAllByIsActive(boolean isActive);
    int countAllBy();

    @Query(value = """

            SELECT s.service_id, s.service_name, IFNULL(report.hours_worked,0) as hours_worked
                                              FROM services s
                                                       LEFT JOIN (SELECT s.service_id,
                                                                         s.service_name,
                                                                         CASE sum(hours_worked)
                                                                             WHEN NULL THEN 0
                                                                             ELSE sum(hours_worked)
                                                                             END
                                                                             as hours_worked
                                                                  FROM services s
                                                                           LEFT JOIN orders o
                                                                                     ON s.service_id = o.service_id
                                                                  WHERE (o.order_status = 'IN_PROGRESS'
                                                                      OR o.order_status = 'COMPLETED')
                                                                    AND o.order_date BETWEEN :date_start AND :date_end
                                                                  GROUP BY s.service_id, s.service_name) as report
                                                                 ON s.service_id = report.service_id
                                              ORDER BY report.hours_worked DESC;
                          """, nativeQuery = true)
    List<Object[]> getHoursWorkedByServiceFromOrdersInRange(@Param("date_start") String dateStart, @Param("date_end") String dateEnd);


    @Query(value = """
             SELECT s.service_id, s.service_name, COUNT(o.service_id) AS total_order_request, IFNULL(order_date, 'No Orders') AS date
             FROM services s
                      JOIN (SELECT service_id, order_status, order_date
                                 FROM orders
                                 WHERE order_date BETWEEN :start_date AND :end_date
                                   AND order_status != 'CANCELLED'
                                   AND order_status != 'DECLINED') o
                                ON s.service_id = o.service_id
             GROUP BY s.service_id, s.service_name, IFNULL(order_date, 'No Orders')
             ORDER BY total_order_request
                     DESC;
            """, nativeQuery = true)
    List<Object[]> getTotalOrderRequestByService(@Param("start_date") String dateStart, @Param("end_date") String dateEnd);
}
