package com.corso.springboot.configuration.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectriqueinc.tech/"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/server")
public class ServerController {

    @Value("${frontend.url}")
    private String frontendDomain;


    @RequestMapping(value = "/status",method = RequestMethod.HEAD)
    public ResponseEntity<Void> connectionVerification()  {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendDomain)).build();
    }
}
