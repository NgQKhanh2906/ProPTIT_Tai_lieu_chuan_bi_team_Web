package com.example.quanlytour.service;

import com.example.quanlytour.dto.KhachHangRequest;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.KhachHangRepository;
import com.example.quanlytour.repository.TourRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;
    private final TourRepository tourRepository;

    public KhachHang createKhachHang(KhachHangRequest request) {
        KhachHang khachHang = new KhachHang();
        khachHang.setCustomerName(request.customerName().trim());
        khachHang.setPhone(request.phone());
        khachHang.setEmail(request.email());
        return khachHangRepository.save(khachHang);
    }

    @Transactional
    public void addFavoriteTour(Long khachHangId, Long tourId) {
        KhachHang khachHang = findKhachHang(khachHangId);
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tour id = " + tourId));
        khachHang.addFavoriteTour(tour);
    }

    @Transactional(readOnly = true)
    public List<Tour> getFavoriteTours(Long khachHangId) {
        return new ArrayList<>(findKhachHang(khachHangId).getFavoriteTours());
    }

    public List<KhachHang> searchByName(String name) {
        return khachHangRepository.findByCustomerNameContainingIgnoreCase(name.trim());
    }

    private KhachHang findKhachHang(Long khachHangId) {
        return khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang id = " + khachHangId));
    }

}