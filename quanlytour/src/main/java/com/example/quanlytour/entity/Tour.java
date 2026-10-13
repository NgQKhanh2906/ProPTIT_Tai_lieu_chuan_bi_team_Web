package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "tours")
@Data
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ten tour khong duoc de trong")
    @Column(name = "tour_name", nullable = false, length = 200)
    private String tourName;

    @Column(name = "departure_location", length = 100)
    private String departureLocation;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "description", length = 1000)
    private String description;

}