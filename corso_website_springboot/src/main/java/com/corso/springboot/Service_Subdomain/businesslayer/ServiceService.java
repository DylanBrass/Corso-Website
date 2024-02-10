package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.DatabaseDTOTotalOrdersPerService;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceTimeByResponse;

import java.util.List;

public interface ServiceService {
    ServiceResponse getServiceByServiceId(String serviceId);

    List<ServiceResponse> getAllServices();

    List<ServiceResponse> getAllVisibleServices();

    ServiceResponse createService(ServiceRequest serviceRequest);

    ServiceResponse modifyService(String serviceId, ServiceRequest serviceRequest);

    ServiceResponse changeServiceVisibility(String serviceId);

    int countAllServices();

    List<ServiceTimeByResponse> getServiceTimeByService(String dateStart, String dateEnd);

    String getTotalOrderRequestByService(String dateStart, String dateEnd);

    List<DatabaseDTOTotalOrdersPerService> getTotalOrdersPerService(String dateStart, String dateEnd);
}
