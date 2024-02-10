package com.corso.springboot.Order_Subdomain.presentationlayer.OrdersPerMonth;


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
public class OrdersPerMonth {

    Long month;

    Long year;

    Long totalOrders;

    public OrdersPerMonth(Object[] tuple) {
        if (tuple.length != 3) {
            throw new IllegalArgumentException("Invalid tuple length: " + tuple.length);
        }
        if (tuple[0].getClass() != Long.class) {
            throw new IllegalArgumentException("Invalid type for month: " + tuple[0].getClass());
        }

        if (tuple[1].getClass() != Long.class) {
            throw new IllegalArgumentException("Invalid type for year: " + tuple[1].getClass());
        }

        if (tuple[2].getClass() != Long.class) {
            throw new IllegalArgumentException("Invalid type for totalOrders: " + tuple[2].getClass());
        }


        this.month = (Long) tuple[0];

        this.year = (Long) tuple[1];

        this.totalOrders = (Long) tuple[2];


    }
}
