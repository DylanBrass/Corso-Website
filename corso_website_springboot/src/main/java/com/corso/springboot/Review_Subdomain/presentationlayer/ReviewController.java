package com.corso.springboot.Review_Subdomain.presentationlayer;


import com.corso.springboot.Review_Subdomain.businesslayer.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectricdev.tech/"},allowCredentials = "true")
@Slf4j
@RestController
@RequestMapping("api/v1/corso/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping()
    public ResponseEntity<List<ReviewResponse>> getAllReviews(){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getAllReviews());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewResponse>> getReviewsByCustomerFullName(@RequestParam(value = "customerFullName", required = false) String customerFullName){
        if (customerFullName != null && !customerFullName.isEmpty()) {
            List<ReviewResponse> reviews = reviewService.getReviewsByCustomerFullName(customerFullName);
            return ResponseEntity.ok(reviews);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

   @GetMapping("/pinned")
    public ResponseEntity<List<ReviewResponse>> getPinnedReviews(@CookieValue(value = "id_token", required = false) String idToken){
        log.info("Get pinned reviews : {}", idToken);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getPinnedReviews(true));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('Customer')")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@AuthenticationPrincipal OidcUser principal){
        String userId = principal.getSubject();
        log.info("Get reviews with userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsByUserId(userId));
    }

}
