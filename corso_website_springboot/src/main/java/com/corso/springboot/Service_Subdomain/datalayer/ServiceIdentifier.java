package com.corso.springboot.Service_Subdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ServiceIdentifier {

    private String serviceId;

    public ServiceIdentifier() {
        this.serviceId = java.util.UUID.randomUUID().toString();
    }

    public ServiceIdentifier(String serviceId) {
        this.serviceId = serviceId;
    }

}