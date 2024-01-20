package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectricdev.tech/"},allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/corso/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/filter")
    public ResponseEntity<List<OrderResponse>> GetAllOrders(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "offset") int offset) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders(queryParams, pageSize, offset));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> GetAllOrdersByStatus(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "offset") int offset) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByStatus(queryParams, pageSize, offset));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/overdue")
    public ResponseEntity<List<OrderResponse>> GetAllOverdueOrders( @RequestParam(name = "pageSize") int pageSize,
                                                                    @RequestParam(name = "offset") int offset) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOverdueOrders(pageSize, offset));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/external")
    public ResponseEntity<OrderResponse> createOrderExternal(@Valid @RequestBody OrderRequestExternal orderRequestExternal) throws IOException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderExternal(orderRequestExternal));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/manage/{orderId}")
    public ResponseEntity<OrderResponse> GetOrderByOrderId(@PathVariable String orderId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByOrderId(orderId));
    }


    @GetMapping("/current")
    public ResponseEntity<List<OrderResponse>> GetCustomerOrders(@AuthenticationPrincipal OidcUser principal) {

        String userId = principal.getSubject();

        return ResponseEntity.status(HttpStatus.OK).body(orderService.getCustomerCurrentOrders(userId, Status.IN_PROGRESS));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/acceptOrder/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderStatusToAccepted(@RequestBody AcceptOrderRequest acceptOrderRequest, @PathVariable String orderId) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatusToAccepted(orderId, acceptOrderRequest));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/manage/count")
    public ResponseEntity<Integer> CountByOrderStatus(@RequestParam(name = "status") String status){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.countByOrderStatus(status));
    }


    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/all/count")
    public ResponseEntity<Integer> CountAllOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.countAllOrders());
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/declineOrder/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderStatusToDeclined(@PathVariable String orderId, @RequestBody DeclineOrderRequest declineOrderRequest) throws MessagingException {
        log.info("Decline order request {}", declineOrderRequest.getReasonForDecline());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatusToDeclined(orderId, declineOrderRequest));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/cancelOrder/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderStatusToCancelled(@PathVariable String orderId, @RequestBody DeclineOrderRequest declineOrderRequest) throws MessagingException {
        log.info("Cancel order request {}", declineOrderRequest.getReasonForDecline());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatusToCancelled(orderId, declineOrderRequest));
    }

}
