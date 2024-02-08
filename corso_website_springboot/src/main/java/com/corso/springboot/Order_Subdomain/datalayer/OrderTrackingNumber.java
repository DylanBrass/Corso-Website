package com.corso.springboot.Order_Subdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.stream.IntStream;

@Getter
@Embeddable
public class OrderTrackingNumber {

    private String orderTrackingNumber;

    public OrderTrackingNumber() {
        this.orderTrackingNumber = generateRandomString(6);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        SecureRandom random = new SecureRandom();

        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(characters.charAt(random.nextInt(characters.length()))))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
