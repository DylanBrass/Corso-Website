package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> GetAllOrdersByStatus(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "offset") int offset) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByStatus(queryParams, pageSize, offset));
    }


    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/external")
    public ResponseEntity<OrderResponse> createOrderExternal(@RequestBody OrderRequestExternal orderRequestExternal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderExternal(orderRequestExternal));
    }
}
