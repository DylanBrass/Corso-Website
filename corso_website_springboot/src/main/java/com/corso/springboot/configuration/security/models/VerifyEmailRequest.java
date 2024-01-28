package com.corso.springboot.configuration.security.models;


import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Generated
@Builder
public class VerifyEmailRequest {

    private String email;
    private String token;
}
