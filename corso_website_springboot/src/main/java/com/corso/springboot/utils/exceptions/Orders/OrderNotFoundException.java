package com.corso.springboot.utils.exceptions.Orders;

public class OrderNotFoundException
    extends RuntimeException{

    public OrderNotFoundException(String message) {
        super(message);
    }
}
