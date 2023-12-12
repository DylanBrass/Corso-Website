package com.corso.springboot.configuration.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Generated
public class UserResponse {
    private String username;
    private String email;
    private List<String> roles;
}
