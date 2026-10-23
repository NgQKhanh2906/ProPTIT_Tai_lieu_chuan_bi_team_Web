package com.example.quanlytour.controller;

import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.service.HoaDonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @PostMapping
    public HoaDonResponse bookTicket(@Valid @RequestBody BookTicketRequest request) {
        return hoaDonService.bookTicket(request);
    }

}