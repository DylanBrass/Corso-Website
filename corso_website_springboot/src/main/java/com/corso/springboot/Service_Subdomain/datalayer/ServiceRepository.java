package com.corso.springboot.Service_Subdomain.datalayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.security.Provider;
import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {
    ServiceEntity findByServiceIdentifier_ServiceId(String serviceId);
    List<ServiceEntity> findAllByIsActive(boolean isActive);
    int countAllBy();
}
