package com.example.quanlytour.repository;

import com.example.quanlytour.entity.HoaDon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("SELECT h FROM HoaDon h JOIN FETCH h.khachHang JOIN FETCH h.lichTrinh l JOIN FETCH l.tour WHERE h.totalAmount > :minTotal ORDER BY h.totalAmount DESC")
    List<HoaDon> findInvoicesWithTotalGreaterThan(@Param("minTotal") Double minTotal);

    @Query("SELECT h FROM HoaDon h JOIN FETCH h.khachHang JOIN FETCH h.lichTrinh l JOIN FETCH l.tour WHERE h.id = :id")
    Optional<HoaDon> findDetailById(@Param("id") Long id);

    @Query("SELECT h FROM HoaDon h JOIN FETCH h.khachHang k JOIN FETCH h.lichTrinh l JOIN FETCH l.tour WHERE h.id = :id AND k.id = :khachHangId")
    Optional<HoaDon> findDetailByIdAndKhachHangId(@Param("id") Long id, @Param("khachHangId") Long khachHangId);

    @Query("SELECT h FROM HoaDon h JOIN FETCH h.khachHang k JOIN FETCH h.lichTrinh l JOIN FETCH l.tour WHERE k.id = :khachHangId ORDER BY h.createdAt DESC, h.id DESC")
    List<HoaDon> findAllDetailByKhachHangId(@Param("khachHangId") Long khachHangId);

}