package com.example.quanlytour.controller;

import com.example.quanlytour.dto.TourRequest;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.projection.TourRevenue;
import com.example.quanlytour.service.TourService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public Page<Tour> getAllTours(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return tourService.getAllTours(page, size);
    }

    @GetMapping("/{id}")
    public Tour getTourById(@PathVariable Long id) {
        return tourService.getTourById(id);
    }

    @PostMapping
    public Tour createTour(@Valid @RequestBody TourRequest request) {
        return tourService.createTour(request);
    }

    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable Long id, @Valid @RequestBody TourRequest request) {
        return tourService.updateTour(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
    }

    @GetMapping("/revenue")
    public List<TourRevenue> getRevenueByTour() {
        return tourService.getRevenueByTour();
    }

}