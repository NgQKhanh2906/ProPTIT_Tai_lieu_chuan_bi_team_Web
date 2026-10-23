package com.example.quanlytour.controller;

import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.service.KhachHangService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @PostMapping
    public KhachHang createKhachHang(@Valid @RequestBody KhachHang khachHang) {
        return khachHangService.createKhachHang(khachHang);
    }

    @PostMapping("/{khachHangId}/tour-yeu-thich/{tourId}")
    public void addFavoriteTour(@PathVariable Long khachHangId, @PathVariable Long tourId) {
        khachHangService.addFavoriteTour(khachHangId, tourId);
    }

    @GetMapping("/{khachHangId}/tour-yeu-thich")
    public List<Tour> getFavoriteTours(@PathVariable Long khachHangId) {
        return khachHangService.getFavoriteTours(khachHangId);
    }

}