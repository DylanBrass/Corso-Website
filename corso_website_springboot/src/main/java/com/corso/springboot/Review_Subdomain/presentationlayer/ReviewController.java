package com.corso.springboot.Review_Subdomain.presentationlayer;


import com.corso.springboot.Review_Subdomain.businesslayer.ReviewService;
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

@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"}, allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(@RequestParam Map<String, String> queryParams,
                                                              @RequestParam(name = "pageSize") int pageSize,
                                                              @RequestParam(name = "offset") int offset) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviews(queryParams, pageSize, offset));
    }

    @Cacheable(value = "reviewsPinned", key = "#root.methodName",sync = true)
    @GetMapping("/pinned")
    public ResponseEntity<List<ReviewResponse>> getPinnedReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getPinnedReviews(true));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@AuthenticationPrincipal OidcUser principal) {
        String userId = principal.getSubject();
        log.info("Get reviews with userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByUserId(userId));
    }

    @Caching(evict = {
            @CacheEvict(value = "reviewsPinned", allEntries = true),
            @CacheEvict(value = "reviewCountPinned", allEntries = true)
    })
    @PreAuthorize("hasAuthority('Admin')")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable String reviewId, @RequestBody ReviewUpdateRequest reviewUpdateRequest) throws MessagingException, IOException, InterruptedException {
        log.info("reviewId: {}", reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.updatePinnedReviewByReviewId(reviewId, reviewUpdateRequest));
    }

    @Cacheable(value = "reviewCountPinned", key = "#root.methodName")
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/count/pinned")
    public ResponseEntity<Integer> getPinnedReviewsCount() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getCountAllByState(true));
    }

    @Cacheable(value = "reviewCount", key = "#root.methodName")
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/count")
    public ResponseEntity<Integer> getAllReviewsCount() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getCountAllReviews());
    }


    @CacheEvict(value = "reviewCount", allEntries = true)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<ReviewResponse> customerCreateReview(@AuthenticationPrincipal OidcUser principal, @RequestBody ReviewRequest reviewRequest) {
        String userId = principal.getSubject();
        log.info("Create review with userId: {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.customerCreateReview(userId, reviewRequest));
    }

    @GetMapping("/{reviewId}")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<ReviewResponse> getReviewByReviewIdAndUserId(@AuthenticationPrincipal OidcUser principal, @PathVariable String reviewId){
        String userId = principal.getSubject();
        log.info("Get review with userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewByReviewIdAndUserId(reviewId, userId));
    }

    
    @Caching(evict = {
            @CacheEvict(value = "reviewsPinned", allEntries = true),
            @CacheEvict(value = "reviewCountPinned", allEntries = true),
            @CacheEvict(value = "reviewCount", allEntries = true)       

    })
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<Void> deleteCustomerReview(@AuthenticationPrincipal OidcUser principal, @PathVariable String reviewId) throws MessagingException {
        String userId = principal.getSubject();
        log.info("Delete review with userId: {}", userId);
        reviewService.deleteCustomerReview(reviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
