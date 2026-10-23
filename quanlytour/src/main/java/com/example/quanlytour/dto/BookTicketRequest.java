package com.example.quanlytour.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookTicketRequest(

        @NotNull(message = "Ma khach hang khong duoc de trong")
        Long khachHangId,

        @NotNull(message = "Ma lich trinh khong duoc de trong")
        Long lichTrinhId,

        @NotNull(message = "So luong khach khong duoc de trong")
        @Min(value = 1, message = "So luong khach toi thieu la 1")
        Integer guestCount) {
}