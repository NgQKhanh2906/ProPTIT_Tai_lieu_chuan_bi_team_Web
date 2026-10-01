package com.example.quanlytour.repository;

import com.example.quanlytour.entity.LichTrinh;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LichTrinhRepository extends JpaRepository<LichTrinh, Long> {

    @Query(value = "SELECT lt.* FROM lich_trinh lt INNER JOIN tours t ON lt.ma_tour = t.id WHERE t.destination = :destination AND lt.so_cho_da_dat < lt.so_cho_toi_da", nativeQuery = true)
    List<LichTrinh> findAvailableSchedulesByDestination(@Param("destination") String destination);

}