package com.corso.springboot.email.presentationLayer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Value
@Generated
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailRequest {

    @Email
    @NotBlank(message = "Recipient is required.")
    @NotNull(message = "Recipient is required.")
    String recipient;

    @NotBlank(message = "Subject is required.")
    @NotNull(message = "Subject is required.")
    String subject;

    @NotBlank(message = "Message is required.")
    @NotNull(message = "Message is required.")
    String message;

}
