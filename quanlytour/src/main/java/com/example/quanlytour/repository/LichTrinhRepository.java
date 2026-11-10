package com.example.quanlytour.repository;

import com.example.quanlytour.entity.LichTrinh;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LichTrinhRepository extends JpaRepository<LichTrinh, Long>, LichTrinhRepositoryCustom {

    List<LichTrinh> findByTourIdAndDepartureDateAfterOrderByDepartureDateAsc(Long tourId, LocalDate date);

    @EntityGraph(attributePaths = {"tour"})
    @Query("SELECT l FROM LichTrinh l ORDER BY l.id")
    List<LichTrinh> findAllWithTour();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LichTrinh l WHERE l.id = :id")
    Optional<LichTrinh> findByIdForUpdate(@Param("id") Long id);

}