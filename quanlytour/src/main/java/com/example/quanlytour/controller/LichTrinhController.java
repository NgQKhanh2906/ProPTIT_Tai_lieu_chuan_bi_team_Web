package com.example.quanlytour.controller;

import com.example.quanlytour.dto.LichTrinhResponse;
import com.example.quanlytour.dto.LichTrinhSearchRequest;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.service.LichTrinhService;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lich-trinh")
public class LichTrinhController {

    private final LichTrinhService lichTrinhService;

    public LichTrinhController(LichTrinhService lichTrinhService) {
        this.lichTrinhService = lichTrinhService;
    }

    @GetMapping
    public List<LichTrinhResponse> getAllLichTrinh() {
        return lichTrinhService.getAllLichTrinh();
    }

    @GetMapping("/{id}")
    public LichTrinhResponse getLichTrinhById(@PathVariable Long id) {
        return lichTrinhService.getLichTrinhResponseById(id);
    }

    @GetMapping("/tour/{tourId}/upcoming")
    public List<LichTrinhResponse> getUpcomingSchedules(@PathVariable Long tourId) {
        return lichTrinhService.getUpcomingSchedules(tourId);
    }

    @PostMapping
    public LichTrinh createLichTrinh(@Valid @RequestBody LichTrinh lichTrinh) {
        return lichTrinhService.createLichTrinh(lichTrinh);
    }

    @PutMapping("/{id}")
    public LichTrinh updateLichTrinh(@PathVariable Long id, @Valid @RequestBody LichTrinh lichTrinh) {
        return lichTrinhService.updateLichTrinh(id, lichTrinh);
    }

    @DeleteMapping("/{id}")
    public void deleteLichTrinh(@PathVariable Long id) {
        lichTrinhService.deleteLichTrinh(id);
    }

    @GetMapping("/search")
    public List<LichTrinhResponse> search(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minAvailableSlots) {
        LichTrinhSearchRequest request = new LichTrinhSearchRequest(destination, fromDate, maxPrice, minAvailableSlots);
        return lichTrinhService.search(request);
    }

    @PostMapping("/{id}/book")
    public LichTrinh bookSlots(@PathVariable Long id, @RequestParam Integer quantity) {
        return lichTrinhService.bookSlots(id, quantity);
    }

}