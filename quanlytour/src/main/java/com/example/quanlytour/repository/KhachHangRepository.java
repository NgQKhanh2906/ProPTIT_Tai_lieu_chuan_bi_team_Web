package com.example.quanlytour.repository;

import com.example.quanlytour.entity.KhachHang;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    List<KhachHang> findByCustomerNameContainingIgnoreCase(String customerName);

}