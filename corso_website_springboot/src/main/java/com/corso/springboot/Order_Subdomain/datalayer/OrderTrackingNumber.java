package com.corso.springboot.Order_Subdomain.datalayer;

import jakarta.persistence.Embeddable;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Embeddable
public class OrderTrackingNumber {

    private String orderTrackingNumber;

    public OrderTrackingNumber() {
        this.orderTrackingNumber = generateRandomString(6);
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        SecureRandom random = new SecureRandom();

        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(characters.charAt(random.nextInt(characters.length()))))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
