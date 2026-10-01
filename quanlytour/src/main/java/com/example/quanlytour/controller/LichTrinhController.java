package com.example.quanlytour.controller;

import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.service.LichTrinhService;
import java.util.List;
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
    public List<LichTrinh> getAllLichTrinh() {
        return lichTrinhService.getAllLichTrinh();
    }

    @GetMapping("/{id}")
    public LichTrinh getLichTrinhById(@PathVariable Long id) {
        return lichTrinhService.getLichTrinhById(id);
    }

    @PostMapping
    public LichTrinh createLichTrinh(@RequestBody LichTrinh lichTrinh) {
        return lichTrinhService.createLichTrinh(lichTrinh);
    }

    @PutMapping("/{id}")
    public LichTrinh updateLichTrinh(@PathVariable Long id, @RequestBody LichTrinh lichTrinh) {
        return lichTrinhService.updateLichTrinh(id, lichTrinh);
    }

    @DeleteMapping("/{id}")
    public void deleteLichTrinh(@PathVariable Long id) {
        lichTrinhService.deleteLichTrinh(id);
    }

    @GetMapping("/search")
    public List<LichTrinh> searchAvailableSchedules(@RequestParam String destination) {
        return lichTrinhService.searchAvailableSchedulesByDestination(destination);
    }

    @PostMapping("/{id}/book")
    public LichTrinh bookSlots(@PathVariable Long id, @RequestParam Integer quantity) {
        return lichTrinhService.bookSlots(id, quantity);
    }

}