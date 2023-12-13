package com.corso.springboot.Service_Subdomain.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class ServiceIdentifier {

    private String serviceId;

    public ServiceIdentifier() {
        this.serviceId = java.util.UUID.randomUUID().toString();
    }

    public String getServiceId() {
        return serviceId;
    }
}