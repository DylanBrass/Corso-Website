package com.corso.springboot.Order_Subdomain.presentationlayer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@Data
@Builder
public class DeclineOrderRequest {
    @Email
    public String recipient;

    @NotBlank
    @NotNull
    public String reasonForDecline;

    public DeclineOrderRequest(String recipient, String reasonForDecline) {
        this.recipient = recipient;
        this.reasonForDecline = reasonForDecline;
    }

    public DeclineOrderRequest() {
    }
}
