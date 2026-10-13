package com.example.quanlytour.service;

import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.repository.LichTrinhRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LichTrinhService {

    private final LichTrinhRepository lichTrinhRepository;
    private final TourService tourService;

    public LichTrinhService(LichTrinhRepository lichTrinhRepository, TourService tourService) {
        this.lichTrinhRepository = lichTrinhRepository;
        this.tourService = tourService;
    }

    public List<LichTrinh> getAllLichTrinh() {
        return lichTrinhRepository.findAll();
    }

    public LichTrinh getLichTrinhById(Long id) {
        return lichTrinhRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay lich trinh co id: " + id));
    }

    public LichTrinh createLichTrinh(LichTrinh lichTrinh) {
        tourService.getTourById(lichTrinh.getMaTour());
        if (lichTrinh.getBookedSlots() == null) {
            lichTrinh.setBookedSlots(0);
        }
        return lichTrinhRepository.save(lichTrinh);
    }

    public LichTrinh updateLichTrinh(Long id, LichTrinh lichTrinhDetails) {
        LichTrinh existingLichTrinh = getLichTrinhById(id);
        tourService.getTourById(lichTrinhDetails.getMaTour());
        existingLichTrinh.setMaTour(lichTrinhDetails.getMaTour());
        existingLichTrinh.setDepartureDate(lichTrinhDetails.getDepartureDate());
        existingLichTrinh.setBasePrice(lichTrinhDetails.getBasePrice());
        existingLichTrinh.setTotalSlots(lichTrinhDetails.getTotalSlots());
        return lichTrinhRepository.save(existingLichTrinh);
    }

    public void deleteLichTrinh(Long id) {
        LichTrinh existingLichTrinh = getLichTrinhById(id);
        lichTrinhRepository.deleteById(existingLichTrinh.getId());
    }

    public List<LichTrinh> searchAvailableSchedulesByDestination(String destination) {
        return lichTrinhRepository.findAvailableSchedulesByDestination(destination);
    }

    @Transactional
    public LichTrinh bookSlots(Long id, Integer quantity) {
        LichTrinh lichTrinh = getLichTrinhById(id);
        int remainingSlots = lichTrinh.getTotalSlots() - lichTrinh.getBookedSlots();
        if (remainingSlots < quantity) {
            throw new IllegalStateException("Khong du cho trong. Con lai: " + remainingSlots);
        }
        lichTrinh.setBookedSlots(lichTrinh.getBookedSlots() + quantity);
        return lichTrinhRepository.save(lichTrinh);
    }

}