package com.corso.springboot.configuration.security.models;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Generated
@Builder
public class UserResponse {
    private String username;
    private String email;
    private List<String> roles;
}
