package com.example.quanlytour.service;

import com.example.quanlytour.dto.TourRequest;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.TourRepository;
import com.example.quanlytour.repository.projection.TourRevenue;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public Page<Tour> getAllTours(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tourRepository.findAll(pageable);
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tour co id: " + id));
    }

    public Tour createTour(TourRequest request) {
        Tour tour = new Tour();
        applyRequest(tour, request);
        return tourRepository.save(tour);
    }

    public Tour updateTour(Long id, TourRequest request) {
        Tour existingTour = getTourById(id);
        applyRequest(existingTour, request);
        return tourRepository.save(existingTour);
    }

    public void deleteTour(Long id) {
        Tour existingTour = getTourById(id);
        tourRepository.deleteById(existingTour.getId());
    }

    public List<TourRevenue> getRevenueByTour() {
        return tourRepository.getRevenueByTour();
    }

    private void applyRequest(Tour tour, TourRequest request) {
        tour.setTourName(request.tourName().trim());
        tour.setDepartureLocation(request.departureLocation());
        tour.setDestination(request.destination());
        tour.setDescription(request.description());
    }

}