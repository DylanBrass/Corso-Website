package com.corso.springboot.Service_Subdomain.presentationlayer;

import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable String serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceByServiceId(serviceId));
    }

    @GetMapping("")
    public ResponseEntity<List<ServiceResponse>> getAllServices(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllServices());
    }
}
