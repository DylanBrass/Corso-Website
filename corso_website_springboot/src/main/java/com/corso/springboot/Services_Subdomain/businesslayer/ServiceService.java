package com.corso.springboot.Services_Subdomain.businesslayer;

import com.corso.springboot.Services_Subdomain.presentationlayer.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();
}
