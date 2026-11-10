package com.example.quanlytour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KhachHangRequest(

        @NotBlank(message = "Ten khach hang khong duoc de trong")
        @Size(max = 100, message = "Ten khach hang toi da 100 ky tu")
        String customerName,

        @Pattern(regexp = "^0\\d{9}$", message = "So dien thoai gom 10 chu so va bat dau bang 0")
        String phone,

        @Email(message = "Email khong dung dinh dang")
        @Size(max = 100, message = "Email toi da 100 ky tu")
        String email) {
}