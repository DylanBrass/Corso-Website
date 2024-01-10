package com.corso.springboot.Order_Subdomain.presentationlayer;

import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@Data
@Builder
public class AcceptOrderRequest {

    @Email
    public String recipient;

    public AcceptOrderRequest(String recipient) {
        this.recipient = recipient;
    }

    public AcceptOrderRequest() {
    }

}
