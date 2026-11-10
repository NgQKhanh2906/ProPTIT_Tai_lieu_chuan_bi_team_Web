package com.example.quanlytour.controller;

import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.service.HoaDonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @PostMapping
    public HoaDonResponse bookTicket(@Valid @RequestBody BookTicketRequest request,
                                     @AuthenticationPrincipal User currentUser) {
        return hoaDonService.bookTicket(request, currentUser.getUsername());
    }

    @GetMapping("/search")
    public List<HoaDonResponse> searchByMinTotal(@RequestParam Double minTotal) {
        return hoaDonService.searchByMinTotal(minTotal);
    }

    @GetMapping("/me")
    public List<HoaDonResponse> getMyInvoices(@AuthenticationPrincipal User currentUser) {
        return hoaDonService.getMyInvoices(currentUser.getUsername());
    }

    @GetMapping("/{id}")
    public HoaDonResponse getHoaDon(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return hoaDonService.getHoaDon(id, currentUser.getUsername());
    }

}