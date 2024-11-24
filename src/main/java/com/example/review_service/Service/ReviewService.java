package com.example.review_service.Service;

import com.example.review_service.Dto.FlightDTO;
import com.example.review_service.Dto.ReservationDTO;
import com.example.review_service.Repository.ReviewRepository;
import com.example.review_service.entity.Review;

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

    public Review createReview(Review review) {

        // Fetch Flight details from the Flight service using the flightId in the review
        FlightDTO flightDTO = restTemplate.getForObject(FLIGHT_SERVICE_URL + review.getFlightId(), FlightDTO.class);
        if (flightDTO == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + review.getFlightId());
        }

        // Fetch Reservation details from the Reservation service using the passenger's cin
        ReservationDTO reservationDTO = restTemplate.getForObject(RESERVATION_SERVICE_URL + review.getCin(), ReservationDTO.class);
        if (reservationDTO == null) {
            throw new IllegalArgumentException("Reservation not found for passenger with CIN: " + review.getCin());
        }

        // Create the review and save it to the database
        return reviewRepository.save(review);
    }
}
