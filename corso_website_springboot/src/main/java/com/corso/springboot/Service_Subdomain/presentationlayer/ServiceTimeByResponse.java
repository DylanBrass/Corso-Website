package com.corso.springboot.Service_Subdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@Value
@Slf4j
public class ServiceTimeByResponse {


    String serviceId;

    String serviceName;

    Long hours_worked;

    public ServiceTimeByResponse(Object[] tuple) {


        if (tuple.length != 3) {
            throw new IllegalArgumentException("Invalid tuple length: " + tuple.length);
        }

        if (tuple[0].getClass() != String.class) {
            throw new IllegalArgumentException("Invalid type for first field of tuple: " + tuple[0].getClass());
        }
        if (tuple[1].getClass() != String.class) {
            throw new IllegalArgumentException("Invalid type for second field of tuple: " + tuple[1].getClass());
        }

        if (tuple[2].getClass() != BigDecimal.class && tuple[2].getClass() != Long.class) {
            throw new IllegalArgumentException("Invalid type for third field of tuple: " + tuple[2].getClass());
        }


        this.serviceId = (String) tuple[0];
        this.serviceName = (String) tuple[1];
        this.hours_worked = Long.parseLong(tuple[2].toString());
    }

}
