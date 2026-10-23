package com.example.quanlytour.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tours")
@Getter
@Setter
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ten tour khong duoc de trong")
    @Column(name = "tour_name", nullable = false, length = 200)
    private String tourName;

    @Column(name = "departure_location", length = 100)
    private String departureLocation;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "description", length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "tour", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<LichTrinh> lichTrinhs = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "favoriteTours")
    private Set<KhachHang> favoritedBy = new HashSet<>();

    public void addLichTrinh(LichTrinh lichTrinh) {
        lichTrinhs.add(lichTrinh);
        lichTrinh.setTour(this);
    }

}