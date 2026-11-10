package com.example.quanlytour.dto;

import java.time.LocalDate;

public record LichTrinhSearchRequest(
        String destination,
        LocalDate fromDate,
        Double maxPrice,
        Integer minAvailableSlots) {
}
