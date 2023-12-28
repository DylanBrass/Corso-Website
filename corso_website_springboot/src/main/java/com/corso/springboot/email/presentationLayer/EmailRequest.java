package com.corso.springboot.email.presentationLayer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailRequest {

    @Email
    @NotBlank(message = "Recipient is required.")
    String recipient;

    @NotBlank(message = "Subject is required.")
    String subject;

    @NotBlank(message = "Message is required.")
    String message;

}
