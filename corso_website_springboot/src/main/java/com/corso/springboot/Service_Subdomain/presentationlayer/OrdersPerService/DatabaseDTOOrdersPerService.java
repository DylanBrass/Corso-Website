package com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.sql.Date;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@Value
@Slf4j
public class DatabaseDTOOrdersPerService {

    String serviceId;

    String serviceName;

    Long totalOrderRequest;

    LocalDate date;

    public DatabaseDTOOrdersPerService(Object[] tuple) {
        if (tuple.length != 4) {
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
                .class) {
            throw new IllegalArgumentException("Invalid type for third field of tuple: " + tuple[2].getClass());
        }


        if (tuple[3].getClass() != String
                .class && tuple[3].getClass() != Date.class) {
            throw new IllegalArgumentException("Invalid type for fourth field of tuple: " + tuple[3].getClass());
        }


        this.serviceId = (String) tuple[0];
        this.serviceName = (String) tuple[1];
        this.totalOrderRequest = (Long) tuple[2];
        this.date = LocalDate.parse(tuple[3].toString());
    }

}
