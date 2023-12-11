package com.corso.springboot.configuration.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Dictionary;
import java.util.HashMap;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String username;
    private String email;
    private String picture;
    private HashMap<String, String> user_metadata;

}
