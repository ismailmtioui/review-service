package com.example.review_service.Service;

import com.example.review_service.Dto.FlightDTO;
import com.example.review_service.Dto.ReservationDTO;
import com.example.review_service.Repository.ReviewRepository;
import com.example.review_service.entity.Review;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String FLIGHT_SERVICE_URL = "http://localhost:8080/api/flights/";
    private static final String RESERVATION_SERVICE_URL = "http://localhost:8081/api/reservations/passenger/";

    @CircuitBreaker(name = "flightServiceCircuitBreaker", fallbackMethod = "fallbackForFlightService")
    @Retry(name = "flightServiceRetry")
    public FlightDTO fetchFlightDetails(Long flightId) {
        return restTemplate.getForObject(FLIGHT_SERVICE_URL + flightId, FlightDTO.class);
    }

    @CircuitBreaker(name = "reservationServiceCircuitBreaker", fallbackMethod = "fallbackForReservationService")
    @Retry(name = "reservationServiceRetry")
    public ReservationDTO fetchReservationDetails(String cin) {
        return restTemplate.getForObject(RESERVATION_SERVICE_URL + cin, ReservationDTO.class);
    }

    public Review createReview(Review review) {

        // Fetch Flight details from the Flight service using the flightId in the review
        FlightDTO flightDTO = fetchFlightDetails(review.getFlightId());
        if (flightDTO == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + review.getFlightId());
        }

        // Fetch Reservation details from the Reservation service using the passenger's cin
        ReservationDTO reservationDTO = fetchReservationDetails(review.getCin());
        if (reservationDTO == null) {
            throw new IllegalArgumentException("Reservation not found for passenger with CIN: " + review.getCin());
        }

        // Create the review and save it to the database
        return reviewRepository.save(review);
    }

    // Fallback method for flight service
    public FlightDTO fallbackForFlightService(Long flightId, Throwable throwable) {
        // Log the issue and return a default response
        System.err.println("Flight service is unavailable: " + throwable.getMessage());
        return null; // Or a default FlightDTO object
    }

    // Fallback method for reservation service
    public ReservationDTO fallbackForReservationService(String cin, Throwable throwable) {
        // Log the issue and return a default response
        System.err.println("Reservation service is unavailable: " + throwable.getMessage());
        return null; // Or a default ReservationDTO object
    }
}

