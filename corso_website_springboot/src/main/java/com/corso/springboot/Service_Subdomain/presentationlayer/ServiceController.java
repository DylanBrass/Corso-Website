package com.corso.springboot.Service_Subdomain.presentationlayer;

import com.corso.springboot.Service_Subdomain.businesslayer.ServiceService;
import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.DatabaseDTOTotalOrdersPerService;
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

@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"}, allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Cacheable(value = "service", key = "#serviceId")
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable String serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceByServiceId(serviceId));
    }


    @GetMapping("")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllServices());
    }

    @Cacheable(value = "services", key = "#root.methodName", sync = true)
    @GetMapping("/visible")
    public ResponseEntity<List<ServiceResponse>> getAllVisibleServices() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllVisibleServices());
    }

    @Caching(evict = {
            @CacheEvict(value = "services", allEntries = true),
            @CacheEvict(value = "servicesCount", allEntries = true),
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true),
            @CacheEvict(value = "service", allEntries = true)
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(serviceRequest));
    }

    @Cacheable(value = "servicesCount", key = "#root.methodName")
    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<Integer> countAllServices() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.countAllServices());
    }

    @Caching(evict = {
            @CacheEvict(value = "services", allEntries = true),
            @CacheEvict(value = "servicesCount", allEntries = true),
            @CacheEvict(value = "service", key = "#serviceId"),
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true),
            @CacheEvict(value = "service", allEntries = true)

    })
    @PutMapping("/{serviceId}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> modifyService(@PathVariable String serviceId, @Valid @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.modifyService(serviceId, serviceRequest));
    }

    @Caching(evict = {
            @CacheEvict(value = "services", allEntries = true),
            @CacheEvict(value = "servicesCount", allEntries = true),
            @CacheEvict(value = "service", key = "#serviceId"),
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true),
            @CacheEvict(value = "servicesCount", allEntries = true)
    })
    @PatchMapping("/{serviceId}/visibility")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<ServiceResponse> changeServiceVisibility(@PathVariable String serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.changeServiceVisibility(serviceId));
    }


    @Cacheable(value = "serviceTimeByService", key = "#root.methodName + #dateStart + #dateEnd")
    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/time-by-service")
    public ResponseEntity<List<ServiceTimeByResponse>> getServiceTimeByService(@RequestParam(name = "date_start") String dateStart, @RequestParam(name = "date_end") String dateEnd) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceTimeByService(dateStart, dateEnd));
    }

    @Cacheable(value = "totalRequestByService", key = "#root.methodName + #dateStart + #dateEnd")
    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping(value = "/orders-per-service",produces = "application/json")
    public ResponseEntity<String> getTotalOrderRequestByService(@RequestParam(name = "date_start") String dateStart, @RequestParam(name = "date_end") String dateEnd) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getTotalOrderRequestByService(dateStart, dateEnd));
    }

    @Cacheable(value = "totalOrdersPerService", key = "#root.methodName + #dateStart + #dateEnd")
    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/total-orders-per-service")
    public ResponseEntity<List<DatabaseDTOTotalOrdersPerService>> getTotalOrdersPerService(@RequestParam(name = "date_start") String dateStart, @RequestParam(name = "date_end") String dateEnd) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getTotalOrdersPerService(dateStart, dateEnd));
    }

}

