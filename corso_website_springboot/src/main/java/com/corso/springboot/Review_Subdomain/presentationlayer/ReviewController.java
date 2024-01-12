package com.corso.springboot.Review_Subdomain.presentationlayer;


import com.corso.springboot.Review_Subdomain.businesslayer.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","https://corso-dev-frontend-xtejdm3liq-nn.a.run.app/"},allowCredentials = "true")
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
    public ResponseEntity<List<ReviewResponse>> getPinnedReviews(){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getPinnedReviews(true));
    }

}
