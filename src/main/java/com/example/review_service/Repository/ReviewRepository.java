package com.example.review_service.Repository;

import com.example.review_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFlightId(Long flightId);
    List<Review> findByCin(String cin);
}
