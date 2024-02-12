package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrdersPerMonth.OrdersPerMonth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectriqueinc.tech/"},allowCredentials = "true")
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

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
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

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
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

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/declineOrder/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderStatusToDeclined(@PathVariable String orderId, @RequestBody DeclineOrderRequest declineOrderRequest) throws MessagingException {
        log.info("Decline order request {}", declineOrderRequest.getReasonForDecline());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatusToDeclined(orderId, declineOrderRequest));
    }

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/cancelOrder/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderStatusToCancelled(@PathVariable String orderId, @RequestBody DeclineOrderRequest declineOrderRequest) throws MessagingException {
        log.info("Cancel order request {}", declineOrderRequest.getReasonForDecline());

        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderStatusToCancelled(orderId, declineOrderRequest));
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/manage/overdue/count")
    public ResponseEntity<Integer> CountAllOverdueOrders(){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.countAllOverdueOrders());
    }


    @Cacheable(value = "ordersPerMonth", key = "#root.methodName + #year")
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/orders-per-month")
    public ResponseEntity<List<OrdersPerMonth>> getTotalOrderRequestByMonth(@RequestParam("year") int year) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getTotalOrderRequestByMonth(year));
    }

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true),
            @CacheEvict(value = "orders", allEntries = true),
            @CacheEvict(value = "ordersCount", allEntries = true),
            @CacheEvict(value = "overdueOrders", allEntries = true),
            @CacheEvict(value = "totalOrders", allEntries = true),
            @CacheEvict(value = "totalOrdersByStatus", allEntries = true),
            @CacheEvict(value = "totalOrdersByStatusCount", allEntries = true)
    })
    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/manage/{orderId}/permanent")
    public ResponseEntity<Void> DeleteOrderByOrderId(@PathVariable String orderId){
        orderService.deleteOrderByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })
    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/updateProgression/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrderProgressionByOrderId(@PathVariable String orderId,@RequestBody OrderProgressionRequest orderProgressionRequest) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrderProgressionByOrderId(orderId, orderProgressionRequest));
    }


    @Caching(evict = {
            @CacheEvict(value = "serviceTimeByService", allEntries = true),
            @CacheEvict(value = "totalRequestByService", allEntries = true),
            @CacheEvict(value = "ordersPerMonth", allEntries = true),
            @CacheEvict(value = "totalOrdersPerService", allEntries = true)
    })

    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/manage/completedOrder/{orderId}")
    public ResponseEntity<OrderResponse> getCompletedOrdersWithCompletedProgression(@PathVariable String orderId) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.setCustomerOrderIdToCompletedStatus(orderId));
    }

}
