package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Generated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/auth0/manage")
public class Auth0Controller {

    private final Auth0ManagementService auth0ManagementService;

    @PostMapping("/add-admin")
    public ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest userRequest) throws UnirestException {
        return auth0ManagementService.addAdmin(userRequest);
    }

    @GetMapping("/get-userinfo")
    public ResponseEntity<UserResponse> getUserInfo() throws UnirestException {
        return null;
    }
}
