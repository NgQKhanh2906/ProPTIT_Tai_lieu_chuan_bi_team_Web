package com.example.quanlytour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Ten dang nhap khong duoc de trong")
        @Size(min = 4, max = 50, message = "Ten dang nhap tu 4 den 50 ky tu")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Ten dang nhap chi gom chu khong dau, so, dau cham, gach duoi")
        String username,

        @NotBlank(message = "Mat khau khong duoc de trong")
        @Size(min = 6, max = 72, message = "Mat khau tu 6 den 72 ky tu")
        String password,

        @NotBlank(message = "Ten khach hang khong duoc de trong")
        String customerName,

        @Pattern(regexp = "^0\\d{9}$", message = "So dien thoai gom 10 chu so va bat dau bang 0")
        String phone,

        @NotBlank(message = "Email khong duoc de trong")
        @Email(message = "Email khong dung dinh dang")
        String email) {
}