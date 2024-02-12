package com.corso.springboot.Customer_Subdomain.presentationlayer;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderCustomerRequest;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestCustomer;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderResponse;
import com.corso.springboot.Review_Subdomain.businesslayer.ReviewService;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"}, allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/corso/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final OrderService orderService;

    private final ReviewService reviewService;

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
            if (customerService.checkIfCustomerExists(principal.getSubject()))
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
                                                           @Valid @RequestBody CustomerRequest customerRequest) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Create customer with userId: {}", userId);
        String email = principal.getEmail();

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest, userId, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);

    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<CustomerResponse> updateCustomer(@AuthenticationPrincipal OidcUser principal,
                                                           @Valid @RequestBody CustomerRequest customerRequest) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Update customer with userId: {}", userId);
        String email = principal.getEmail();

        CustomerResponse customerResponse = customerService.updateCustomer(customerRequest, userId, email);

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
    public ResponseEntity<OrderResponse> GetOrderByOrderId(@AuthenticationPrincipal OidcUser principal, @PathVariable String orderId) {
        String userId = principal.getSubject();
        log.info("Get customer order with userId: {}", userId);

        return ResponseEntity.ok(orderService.getOrderByIdAndUserId(orderId, userId));
    }

    @PreAuthorize("hasAuthority('Customer')")
    @DeleteMapping(value = "/orders/{orderId}", consumes = "application/json")
    public ResponseEntity<Void> DeleteOrderByOrderId(@PathVariable String orderId, @RequestBody CustomerCancelOrderRequest cancelOrderRequest, @AuthenticationPrincipal OidcUser principal) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Delete customer order with userId: {}", userId);

        orderService.cancelByCustomerOrderByOrderId(orderId, userId, cancelOrderRequest);

        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('Customer')")
    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderByOrderId(@PathVariable String orderId, @RequestBody OrderCustomerRequest customerUpdateOrderRequest, @AuthenticationPrincipal OidcUser principal) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Update customer order with userId: {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderByOrderId(orderId, userId, customerUpdateOrderRequest));
    }


    @PatchMapping("/reviews/{reviewId}")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<ReviewResponse> updateCustomerReview(@AuthenticationPrincipal OidcUser principal, @PathVariable String reviewId, @RequestBody ReviewRequest reviewUpdateRequest) throws MessagingException, IOException, InterruptedException {
        String userId = principal.getSubject();
        log.info("Update review with userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updateCustomerReview(reviewId, userId, reviewUpdateRequest));
    }


    @PostMapping("/request/verification")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<Void> verifyCustomer(@AuthenticationPrincipal OidcUser principal) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Verify customer with userId: {}", userId);

        customerService.verifyCustomer(userId);

        return ResponseEntity.noContent().build();

    }
    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
    @PostMapping("/orders/request")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<OrderResponse> CustomerRequestOrder(@AuthenticationPrincipal OidcUser principal, @Valid @RequestBody OrderRequestCustomer orderRequestCustomer) throws MessagingException {
        String userId = principal.getSubject();
        String userEmail = principal.getEmail();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.CustomerRequestOrder(userId, userEmail, orderRequestCustomer));
    }

}
