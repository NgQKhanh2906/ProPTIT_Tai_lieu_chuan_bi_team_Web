package com.example.quanlytour.service;

import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.entity.HoaDon;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.repository.HoaDonRepository;
import com.example.quanlytour.repository.KhachHangRepository;
import com.example.quanlytour.repository.LichTrinhRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final LichTrinhRepository lichTrinhRepository;

    @Transactional
    public HoaDonResponse bookTicket(BookTicketRequest request) {
        KhachHang khachHang = khachHangRepository.findById(request.khachHangId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang id = " + request.khachHangId()));
        LichTrinh lichTrinh = lichTrinhRepository.findById(request.lichTrinhId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay lich trinh id = " + request.lichTrinhId()));

        int bookedSlots = lichTrinh.getBookedSlots() == null ? 0 : lichTrinh.getBookedSlots();
        if (bookedSlots + request.guestCount() > lichTrinh.getTotalSlots()) {
            throw new IllegalStateException("Khong du cho trong");
        }
        lichTrinh.setBookedSlots(bookedSlots + request.guestCount());

        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setLichTrinh(lichTrinh);
        hoaDon.setGuestCount(request.guestCount());
        hoaDon.setTicketPrice(lichTrinh.getBasePrice());

        return HoaDonResponse.from(hoaDonRepository.save(hoaDon));
    }

}