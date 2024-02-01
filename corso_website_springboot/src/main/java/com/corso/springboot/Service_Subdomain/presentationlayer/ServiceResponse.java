package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@Value
public class ServiceResponse {
    String serviceId;
    String serviceName;
    String serviceDescription;
    String serviceIcon;
    String serviceImage;
    boolean isActive;
}