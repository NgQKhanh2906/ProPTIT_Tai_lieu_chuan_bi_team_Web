package com.example.quanlytour.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.quanlytour.entity.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SqlInjectionDemoTest {

    private static final String INPUT = "' OR '1'='1";

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void concatenationReturnsEveryTourButBindParameterReturnsNothing() {
        String concatenated = "SELECT t FROM Tour t WHERE t.destination = '" + INPUT + "'";
        List<Tour> unsafe = entityManager.createQuery(concatenated, Tour.class).getResultList();

        List<Tour> safe = entityManager.createQuery("SELECT t FROM Tour t WHERE t.destination = :destination", Tour.class)
                .setParameter("destination", INPUT)
                .getResultList();

        long totalTours = entityManager.createQuery("SELECT COUNT(t) FROM Tour t", Long.class).getSingleResult();

        System.out.println("Input nguoi dung nhap : " + INPUT);
        System.out.println("Cau JPQL sau khi noi  : " + concatenated);
        System.out.println("Noi chuoi  -> " + unsafe.size() + " tour: " + unsafe.stream().map(Tour::getTourName).toList());
        System.out.println("Bind tham so -> " + safe.size() + " tour");
        assertThat(unsafe).hasSize((int) totalTours);
        assertThat(safe).isEmpty();
    }

}
