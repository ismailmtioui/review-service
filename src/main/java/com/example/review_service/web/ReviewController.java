package com.example.review_service.web;

import com.example.review_service.Service.ReviewService;
import com.example.review_service.entity.Review;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Attach CircuitBreaker with fallback to the ReviewService
    @PostMapping
    @CircuitBreaker(name = "reviewServiceCircuitBreaker", fallbackMethod = "fallbackForCreateReview")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        // Attempt to create a review using the service
        Review createdReview = reviewService.createReview(review);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // Fallback method for circuit breaker
    public ResponseEntity<String> fallbackForCreateReview(Review review, Throwable throwable) {
        return new ResponseEntity<>("Service temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
