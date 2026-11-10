package com.example.quanlytour.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TourRequest(

        @NotBlank(message = "Ten tour khong duoc de trong")
        @Size(max = 200, message = "Ten tour toi da 200 ky tu")
        String tourName,

        @Size(max = 100, message = "Noi xuat phat toi da 100 ky tu")
        String departureLocation,

        @Size(max = 100, message = "Noi den toi da 100 ky tu")
        String destination,

        @Size(max = 1000, message = "Mo ta toi da 1000 ky tu")
        String description) {
}