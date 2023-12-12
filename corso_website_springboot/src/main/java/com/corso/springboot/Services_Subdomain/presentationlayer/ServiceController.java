package com.corso.springboot.Services_Subdomain.presentationlayer;

import com.corso.springboot.Services_Subdomain.businesslayer.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/allServices")
    public ResponseEntity<List<ServiceResponse>> getAllServices(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllServices());
    }
}
