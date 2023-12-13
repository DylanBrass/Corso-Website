package com.corso.springboot.Order_Subdomain.datalayer;

public enum Status {

    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
