package com.corso.springboot.Service_Subdomain.datalayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {
    Service findByServiceIdentifier_ServiceId(String serviceId);

    int countAllBy();
}
