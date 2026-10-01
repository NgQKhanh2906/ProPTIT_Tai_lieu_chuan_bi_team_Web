package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "lich_trinh")
@Data
public class LichTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_tour", nullable = false)
    private Long maTour;

    @Column(name = "ngay_xuat_phat", nullable = false)
    private LocalDate departureDate;

    @Column(name = "gia_co_ban", nullable = false)
    private Double basePrice;

    @Column(name = "so_cho_toi_da", nullable = false)
    private Integer totalSlots;

    @Column(name = "so_cho_da_dat")
    private Integer bookedSlots;

}