package com.example.quanlytour.service;

import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.TourRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tour co id: " + id));
    }

    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public Tour updateTour(Long id, Tour tourDetails) {
        Tour existingTour = getTourById(id);
        existingTour.setTourName(tourDetails.getTourName());
        existingTour.setDepartureLocation(tourDetails.getDepartureLocation());
        existingTour.setDestination(tourDetails.getDestination());
        existingTour.setDescription(tourDetails.getDescription());
        return tourRepository.save(existingTour);
    }

    public void deleteTour(Long id) {
        Tour existingTour = getTourById(id);
        tourRepository.deleteById(existingTour.getId());
    }

}