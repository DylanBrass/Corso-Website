package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
@AllArgsConstructor
public class ServiceRequest {
    public String serviceName;
    public String serviceDescription;
    public String serviceIcon;
    public String serviceImage;
}
