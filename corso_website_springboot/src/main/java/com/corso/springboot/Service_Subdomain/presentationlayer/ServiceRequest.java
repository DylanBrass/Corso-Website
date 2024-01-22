package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ServiceRequest {
    public String serviceName;
    public String serviceDescription;
    public String serviceIcon;
    public String serviceImage;
    public boolean isActive;
}
