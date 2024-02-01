package com.corso.springboot.Service_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@ActiveProfiles("test")
class ServiceRepositoryTest {

    Logger logger = Logger.getLogger(ServiceRepositoryTest.class.getName());

    @Autowired
    ServiceRepository serviceRepository;

    @Test
    void findByServiceIdentifier_ServiceId() {
        String serviceId = "982577e6-1909-46b8-8583-e08c9daa4e9b";
        ServiceEntity service = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
        assertEquals(serviceId, service.getServiceIdentifier().getServiceId());
    }

    @Test
    void countAllBy() {
        int count = serviceRepository.countAllBy();
        assertEquals(5, count);
    }
}
