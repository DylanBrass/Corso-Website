package com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@Value
@Slf4j
public class DatabaseDTOTotalOrdersPerService {
    String serviceId;
    String serviceName;
    Long totalOrderRequest;

    public DatabaseDTOTotalOrdersPerService(Object[] tuple) {
        if (tuple.length != 3) {
            throw new IllegalArgumentException("Invalid tuple length: " + tuple.length);
        }

        if (tuple[0].getClass() != String
                .class) {
            throw new IllegalArgumentException("Invalid type for first field of tuple: " + tuple[0].getClass());
        }

        if (tuple[1].getClass() != String
                .class) {
            throw new IllegalArgumentException("Invalid type for second field of tuple: " + tuple[1].getClass());
        }

        if (tuple[2].getClass() != Long
                .class && tuple[2].getClass() != Integer.class) {
            throw new IllegalArgumentException("Invalid type for third field of tuple: " + tuple[2].getClass());
        }

        this.serviceId = (String) tuple[0];
        this.serviceName = (String) tuple[1];
        this.totalOrderRequest = (Long) tuple[2];
    }
}
