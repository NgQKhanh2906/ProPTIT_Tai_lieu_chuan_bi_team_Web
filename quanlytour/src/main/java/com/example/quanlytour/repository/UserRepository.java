package com.example.quanlytour.repository;

import com.example.quanlytour.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"khachHang"})
    Optional<User> findWithKhachHangByUsername(String username);

    @EntityGraph(attributePaths = {"khachHang"})
    @Query("SELECT u FROM User u ORDER BY u.id")
    List<User> findAllWithKhachHang();

}