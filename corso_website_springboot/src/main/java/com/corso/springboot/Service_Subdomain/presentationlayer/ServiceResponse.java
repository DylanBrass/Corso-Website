package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.*;

@EqualsAndHashCode(callSuper=false)
//@Data
@AllArgsConstructor
@Builder
@Value
public class ServiceResponse {
    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceIcon;
    private String serviceImage;
}