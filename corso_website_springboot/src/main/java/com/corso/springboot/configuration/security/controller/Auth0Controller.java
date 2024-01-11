package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.security.models.Auth0GetUsersResponse;
import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Generated
@CrossOrigin(origins = {"http://localhost:3000","https://corso-fe.onrender.com"},allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/auth0/manage")
public class Auth0Controller {

    private final Auth0ManagementService auth0ManagementService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/add-admin")
    public ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest userRequest) throws IOException, InterruptedException {
        return auth0ManagementService.addAdmin(userRequest);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/get-total-admins")
    public ResponseEntity<Integer> getTotalAdmins() throws IOException, InterruptedException {
        return auth0ManagementService.getTotalOfRole("rol_5AZt4iZ6HOc5TMa5");
    }


    @GetMapping("/users")
    public ResponseEntity<List<Auth0GetUsersResponse>> getUser(@RequestParam Map<String, String > requestParams) throws IOException, InterruptedException {
        return auth0ManagementService.getMatchingUsers(requestParams);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/get-stats-last-30-days")
    public ResponseEntity<Integer> getStatsLast30Days() {
        return auth0ManagementService.getStatsLast30Days();
    }
}
