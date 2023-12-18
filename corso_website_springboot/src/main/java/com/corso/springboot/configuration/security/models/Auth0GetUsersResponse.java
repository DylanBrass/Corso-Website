package com.corso.springboot.configuration.security.models;


import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@Generated
@Builder
public class Auth0GetUsersResponse {

    String user_id;

    String name;

    String email;

    String username;

    String phone;

    String address;

    String postalCode;

    String city;

    String apartmentNumber;
}
