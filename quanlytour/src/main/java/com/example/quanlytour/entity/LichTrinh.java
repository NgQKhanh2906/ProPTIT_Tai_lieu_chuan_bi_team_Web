package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lich_trinh")
@Getter
@Setter
public class LichTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tour", nullable = false)
    private Tour tour;

    @NotNull(message = "Ngay xuat phat khong duoc de trong")
    @Column(name = "ngay_xuat_phat", nullable = false)
    private LocalDate departureDate;

    @NotNull(message = "Gia co ban khong duoc de trong")
    @Positive(message = "Gia co ban phai lon hon 0")
    @Column(name = "gia_co_ban", nullable = false)
    private Double basePrice;

    @NotNull(message = "So cho toi da khong duoc de trong")
    @Positive(message = "So cho toi da phai lon hon 0")
    @Column(name = "so_cho_toi_da", nullable = false)
    private Integer totalSlots;

    @Column(name = "so_cho_da_dat")
    private Integer bookedSlots;

}