package com.example.review_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;       // Passenger CIN
    private Long flightId;    // Associated Flight ID
    private String comment;   // Review comment
    private Integer rating;   // Rating for the flight

    // Constructors, getters, setters, etc.
}
