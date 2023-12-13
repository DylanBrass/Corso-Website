package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class ServiceResponse {
    private String serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceIcon;
    private String serviceImage;
}