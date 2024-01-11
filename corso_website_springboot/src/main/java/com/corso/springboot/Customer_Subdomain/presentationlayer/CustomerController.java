package com.corso.springboot.Customer_Subdomain.presentationlayer;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000","https://corso-fe.onrender.com"},allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/corso/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final OrderService orderService;

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<Void> deleteCustomer(@AuthenticationPrincipal OidcUser principal) {
        String userId = principal.getSubject();

        log.info("Delete customer with userId: {}", userId);

        customerService.deleteCustomer(userId);

        return ResponseEntity.noContent().build();

    }


    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomerByUserId(@AuthenticationPrincipal OidcUser principal, @RequestParam Map<String, String> requestParams) {
        log.info("Get customer with userId: {}", principal.getSubject());

       if (requestParams.containsKey("simpleCheck") && requestParams.get("simpleCheck").equals("true")) {
            if(customerService.checkIfCustomerExists(principal.getSubject()))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CustomerResponse customerResponse = customerService.getCustomerByUserId(principal.getSubject());

        return ResponseEntity.ok(customerResponse);

    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<CustomerResponse> createCustomer(@AuthenticationPrincipal OidcUser principal,
                                                          @Valid @RequestBody CustomerRequest customerRequest) {
        String userId = principal.getSubject();
        log.info("Create customer with userId: {}", userId);

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);

    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<CustomerResponse> updateCustomer(@AuthenticationPrincipal OidcUser principal,
                                                           @Valid @RequestBody CustomerRequest customerRequest) {
        String userId = principal.getSubject();
        log.info("Update customer with userId: {}", userId);

        CustomerResponse customerResponse = customerService.updateCustomer(customerRequest, userId);

        return ResponseEntity.ok(customerResponse);

    }

    @GetMapping("/orders")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<List<OrderResponse>> getCustomerWithOrders(@AuthenticationPrincipal OidcUser principal, @RequestParam(name = "pageSize") int pageSize,
                                                                     @RequestParam(name = "offset") int offset) {
        String userId = principal.getSubject();
        log.info("Get customer orders with userId: {}", userId);

        return ResponseEntity.ok(orderService.getCustomerOrders(userId, pageSize, offset));
    }

    @PreAuthorize("hasAuthority('Customer')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> GetOrderByOrderId(@AuthenticationPrincipal OidcUser principal, @PathVariable String orderId){
        String userId = principal.getSubject();
        log.info("Get customer order with userId: {}", userId);

        return ResponseEntity.ok(orderService.getOrderByIdAndUserId(orderId, userId));
    }

}
