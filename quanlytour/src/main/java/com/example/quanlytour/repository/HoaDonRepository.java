package com.example.quanlytour.repository;

import com.example.quanlytour.entity.HoaDon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("SELECT h FROM HoaDon h JOIN FETCH h.khachHang JOIN FETCH h.lichTrinh l JOIN FETCH l.tour WHERE h.totalAmount > :minTotal ORDER BY h.totalAmount DESC")
    List<HoaDon> findInvoicesWithTotalGreaterThan(@Param("minTotal") Double minTotal);
}