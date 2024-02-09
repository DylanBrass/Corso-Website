package com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = false)
@Builder
@Value
@Slf4j
@AllArgsConstructor
public class ServiceNameAndTotalDTO {

        String serviceName;

        Long total;



    public ServiceNameAndTotalDTO(DatabaseDTOOrdersPerService databaseDTOOrdersPerService) {
        this.serviceName = databaseDTOOrdersPerService.getServiceName();
        this.total = databaseDTOOrdersPerService.getTotalOrderRequest();
    }
}
