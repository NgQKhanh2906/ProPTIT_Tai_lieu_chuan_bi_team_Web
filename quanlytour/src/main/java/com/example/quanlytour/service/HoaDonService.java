package com.example.quanlytour.service;

import com.example.quanlytour.chat.ConversationService;
import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.entity.HoaDon;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.HoaDonRepository;
import com.example.quanlytour.repository.LichTrinhRepository;
import com.example.quanlytour.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final LichTrinhRepository lichTrinhRepository;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @Transactional
    public HoaDonResponse bookTicket(BookTicketRequest request, String username) {
        User user = findUser(username);
        KhachHang khachHang = findLinkedKhachHang(user);

        LichTrinh lichTrinh = lichTrinhRepository.findByIdForUpdate(request.lichTrinhId())
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
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        conversationService.joinTourGroup(lichTrinh, user);
        return HoaDonResponse.from(savedHoaDon);
    }

    @Transactional(readOnly = true)
    public List<HoaDonResponse> searchByMinTotal(Double minTotal) {
        return hoaDonRepository.findInvoicesWithTotalGreaterThan(minTotal)
                .stream()
                .map(HoaDonResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HoaDonResponse> getMyInvoices(String username) {
        KhachHang khachHang = findLinkedKhachHang(findUser(username));
        return hoaDonRepository.findAllDetailByKhachHangId(khachHang.getId())
                .stream()
                .map(HoaDonResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public HoaDonResponse getHoaDon(Long id, String username) {
        User user = findUser(username);
        return findVisibleHoaDon(id, user)
                .map(HoaDonResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hoa don id = " + id));
    }

    private Optional<HoaDon> findVisibleHoaDon(Long id, User user) {
        if (user.getRole() == Role.ADMIN) {
            return hoaDonRepository.findDetailById(id);
        }
        if (user.getKhachHang() == null) {
            return Optional.empty();
        }
        return hoaDonRepository.findDetailByIdAndKhachHangId(id, user.getKhachHang().getId());
    }

    private User findUser(String username) {
        return userRepository.findWithKhachHangByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay user: " + username));
    }

    private KhachHang findLinkedKhachHang(User user) {
        KhachHang khachHang = user.getKhachHang();
        if (khachHang == null) {
            throw new IllegalStateException("Tai khoan chua gan voi khach hang");
        }
        return khachHang;
    }

}