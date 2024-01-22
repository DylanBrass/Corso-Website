package com.corso.springboot.Service_Subdomain.presentationlayer;

import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectriqueinc.tech/"},allowCredentials = "true")
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

    @GetMapping("/visible")
    public ResponseEntity<List<ServiceResponse>> getAllVisibleServices(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllVisibleServices());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest serviceRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(serviceRequest));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<Integer> countAllServices(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.countAllServices());
    }

    @PutMapping("/{serviceId}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> modifyService(@PathVariable String serviceId, @Valid @RequestBody ServiceRequest serviceRequest){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.modifyService(serviceId, serviceRequest));
    }

    @PatchMapping("/{serviceId}/visibility")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> changeServiceVisibility(@PathVariable String serviceId){
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.changeServiceVisibility(serviceId));
    }

}

