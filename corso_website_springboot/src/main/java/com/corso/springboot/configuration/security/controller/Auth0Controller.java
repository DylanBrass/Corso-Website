package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Generated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/auth0/manage")
public class Auth0Controller {

    private final Auth0ManagementService auth0ManagementService;

    @PostMapping("/add-admin")
    public ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest userRequest) throws IOException, InterruptedException {
        return auth0ManagementService.addAdmin(userRequest);
    }


    @GetMapping("/get-total-admins")
    public ResponseEntity<Integer> getTotalAdmins() throws IOException, InterruptedException {
        return auth0ManagementService.getTotalOfRole("rol_5AZt4iZ6HOc5TMa5");
    }
}
