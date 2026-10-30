package com.example.quanlytour.dto;

import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;

public record UserResponse(
        Long id,
        String username,
        Role role,
        String customerName,
        String phone,
        String email) {

    public static UserResponse from(User user) {
        KhachHang khachHang = user.getKhachHang();
        if (khachHang == null) {
            return new UserResponse(user.getId(), user.getUsername(), user.getRole(), null, null, null);
        }
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                khachHang.getCustomerName(),
                khachHang.getPhone(),
                khachHang.getEmail());
    }

}