package com.example.quanlytour.repository;

import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.projection.TourRevenue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query(value = """
            SELECT t.id AS tourId,
                   t.tour_name AS tourName,
                   COUNT(h.id) AS invoiceCount,
                   COALESCE(SUM(h.tong_tien), 0) AS revenue
            FROM tours t
            LEFT JOIN lich_trinh l ON l.ma_tour = t.id
            LEFT JOIN hoa_don h ON h.ma_lich_trinh = l.id
            GROUP BY t.id, t.tour_name
            ORDER BY revenue DESC
            """, nativeQuery = true)
    List<TourRevenue> getRevenueByTour();

}