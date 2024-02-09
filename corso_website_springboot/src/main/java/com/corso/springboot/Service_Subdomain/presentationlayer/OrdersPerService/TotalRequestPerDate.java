package com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Value
@Slf4j
public class TotalRequestPerDate {

    LocalDate date;

    List<ServiceNameAndTotalDTO> results;


    public TotalRequestPerDate(LocalDate date, List<DatabaseDTOOrdersPerService> databaseDTOOrdersPerService) {
        this.date = date;
        this.results = databaseDTOOrdersPerService.stream()
                .map(ServiceNameAndTotalDTO::new)
                .toList();
    }

}
