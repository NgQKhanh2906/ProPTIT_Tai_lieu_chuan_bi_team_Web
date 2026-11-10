package com.example.quanlytour.dto;

import com.example.quanlytour.entity.LichTrinh;
import java.time.LocalDate;

public record LichTrinhResponse(
        Long id,
        Long tourId,
        String tourName,
        LocalDate departureDate,
        Double basePrice,
        Integer availableSlots) {

    public static LichTrinhResponse from(LichTrinh lichTrinh) {
        int bookedSlots = lichTrinh.getBookedSlots() == null ? 0 : lichTrinh.getBookedSlots();
        return new LichTrinhResponse(
                lichTrinh.getId(),
                lichTrinh.getTour().getId(),
                lichTrinh.getTour().getTourName(),
                lichTrinh.getDepartureDate(),
                lichTrinh.getBasePrice(),
                lichTrinh.getTotalSlots() - bookedSlots);
    }

}