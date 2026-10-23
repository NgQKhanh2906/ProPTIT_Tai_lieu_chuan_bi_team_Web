package com.example.quanlytour.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ten khach hang khong duoc de trong")
    @Column(name = "ten_khach_hang", nullable = false, length = 100)
    private String customerName;

    @Column(name = "so_dien_thoai", length = 15)
    private String phone;

    @Email(message = "Email khong dung dinh dang")
    @Column(name = "email", length = 100)
    private String email;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tour_yeu_thich",
            joinColumns = @JoinColumn(name = "ma_khach_hang"),
            inverseJoinColumns = @JoinColumn(name = "ma_tour")
    )
    private Set<Tour> favoriteTours = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> hoaDons = new ArrayList<>();

    public void addFavoriteTour(Tour tour) {
        favoriteTours.add(tour);
        tour.getFavoritedBy().add(this);
    }

    public void removeFavoriteTour(Tour tour) {
        favoriteTours.remove(tour);
        tour.getFavoritedBy().remove(this);
    }

}