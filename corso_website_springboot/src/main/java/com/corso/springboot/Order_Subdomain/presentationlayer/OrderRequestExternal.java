package com.corso.springboot.Order_Subdomain.presentationlayer;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class OrderRequestExternal {

    @NotBlank
    public String serviceId;

    public String userId;

    private String dueDate;

    private String orderDescription;

    @NotBlank
    private String customerFullName;

    @NotBlank
    private String customerEmail;

    private String customerPhone;

    @NotBlank
    private String customerAddress;
}
