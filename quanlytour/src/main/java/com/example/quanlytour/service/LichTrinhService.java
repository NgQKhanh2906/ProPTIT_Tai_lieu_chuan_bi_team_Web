package com.example.quanlytour.service;

import com.example.quanlytour.dto.LichTrinhResponse;
import com.example.quanlytour.dto.LichTrinhSearchRequest;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.LichTrinhRepository;
import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public List<LichTrinhResponse> getAllLichTrinh() {
        return lichTrinhRepository.findAllWithTour()
                .stream()
                .map(LichTrinhResponse::from)
                .toList();
    }

    public LichTrinh getLichTrinhById(Long id) {
        return lichTrinhRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay lich trinh co id: " + id));
    }

    @Transactional(readOnly = true)
    public LichTrinhResponse getLichTrinhResponseById(Long id) {
        return LichTrinhResponse.from(getLichTrinhById(id));
    }

    @Transactional(readOnly = true)
    public List<LichTrinhResponse> getUpcomingSchedules(Long tourId) {
        return lichTrinhRepository.findByTourIdAndDepartureDateAfterOrderByDepartureDateAsc(tourId, LocalDate.now())
                .stream()
                .map(LichTrinhResponse::from)
                .toList();
    }

    @Transactional
    public LichTrinh createLichTrinh(LichTrinh lichTrinh) {
        Tour tour = resolveTour(lichTrinh);
        lichTrinh.setId(null);
        lichTrinh.setTour(tour);
        lichTrinh.setBookedSlots(0);
        return lichTrinhRepository.save(lichTrinh);
    }

    @Transactional
    public LichTrinh updateLichTrinh(Long id, LichTrinh lichTrinhDetails) {
        LichTrinh existingLichTrinh = getLichTrinhById(id);
        Tour tour = resolveTour(lichTrinhDetails);
        validateSlots(lichTrinhDetails.getTotalSlots(), getBookedSlotsOrZero(existingLichTrinh));
        existingLichTrinh.setTour(tour);
        existingLichTrinh.setDepartureDate(lichTrinhDetails.getDepartureDate());
        existingLichTrinh.setBasePrice(lichTrinhDetails.getBasePrice());
        existingLichTrinh.setTotalSlots(lichTrinhDetails.getTotalSlots());
        return lichTrinhRepository.save(existingLichTrinh);
    }

    public void deleteLichTrinh(Long id) {
        LichTrinh existingLichTrinh = getLichTrinhById(id);
        lichTrinhRepository.deleteById(existingLichTrinh.getId());
    }

    @Transactional(readOnly = true)
    public List<LichTrinhResponse> search(LichTrinhSearchRequest request) {
        return lichTrinhRepository.search(request)
                .stream()
                .map(LichTrinhResponse::from)
                .toList();
    }

    @Transactional
    public LichTrinh bookSlots(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalStateException("So luong dat phai lon hon 0");
        }
        LichTrinh lichTrinh = lichTrinhRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay lich trinh co id: " + id));
        int bookedSlots = getBookedSlotsOrZero(lichTrinh);
        int remainingSlots = lichTrinh.getTotalSlots() - bookedSlots;
        if (remainingSlots < quantity) {
            throw new IllegalStateException("Khong du cho trong. Con lai: " + remainingSlots);
        }
        lichTrinh.setBookedSlots(bookedSlots + quantity);
        return lichTrinhRepository.save(lichTrinh);
    }

    private Tour resolveTour(LichTrinh lichTrinh) {
        if (lichTrinh.getTour() == null || lichTrinh.getTour().getId() == null) {
            throw new IllegalStateException("Lich trinh phai thuoc ve mot tour");
        }
        return tourService.getTourById(lichTrinh.getTour().getId());
    }

    private int getBookedSlotsOrZero(LichTrinh lichTrinh) {
        return lichTrinh.getBookedSlots() == null ? 0 : lichTrinh.getBookedSlots();
    }

    private void validateSlots(Integer totalSlots, int bookedSlots) {
        if (totalSlots != null && totalSlots < bookedSlots) {
            throw new IllegalStateException("So cho toi da khong duoc nho hon so cho da dat: " + bookedSlots);
        }
    }

}