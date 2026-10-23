package com.example.quanlytour.dto;

import com.example.quanlytour.entity.HoaDon;
import com.example.quanlytour.entity.LichTrinh;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record HoaDonResponse(
        Long id,
        String customerName,
        String tourName,
        LocalDate departureDate,
        Integer guestCount,
        Double ticketPrice,
        Double totalAmount,
        LocalDateTime createdAt) {

    public static HoaDonResponse from(HoaDon hoaDon) {
        LichTrinh lichTrinh = hoaDon.getLichTrinh();
        return new HoaDonResponse(
                hoaDon.getId(),
                hoaDon.getKhachHang().getCustomerName(),
                lichTrinh.getTour().getTourName(),
                lichTrinh.getDepartureDate(),
                hoaDon.getGuestCount(),
                hoaDon.getTicketPrice(),
                hoaDon.getTotalAmount(),
                hoaDon.getCreatedAt());
    }

}