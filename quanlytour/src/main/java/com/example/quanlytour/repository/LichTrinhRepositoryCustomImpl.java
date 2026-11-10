package com.example.quanlytour.repository;

import com.example.quanlytour.dto.LichTrinhSearchRequest;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class LichTrinhRepositoryCustomImpl implements LichTrinhRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<LichTrinh> search(LichTrinhSearchRequest request) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LichTrinh> criteriaQuery = criteriaBuilder.createQuery(LichTrinh.class);
        Root<LichTrinh> root = criteriaQuery.from(LichTrinh.class);
        Fetch<LichTrinh, Tour> tourFetch = root.fetch("tour");
        Join<LichTrinh, Tour> tourJoin = (Join<LichTrinh, Tour>) tourFetch;

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(request.destination())) {
            predicates.add(criteriaBuilder.equal(tourJoin.get("destination"), request.destination().trim()));
        }
        if (request.fromDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("departureDate"), request.fromDate()));
        }
        if (request.maxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), request.maxPrice()));
        }
        if (request.minAvailableSlots() != null) {
            Expression<Integer> bookedSlots = criteriaBuilder.coalesce(root.<Integer>get("bookedSlots"), 0);
            Expression<Integer> availableSlots = criteriaBuilder.diff(root.<Integer>get("totalSlots"), bookedSlots);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(availableSlots, request.minAvailableSlots()));
        }

        criteriaQuery.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(criteriaBuilder.asc(root.get("departureDate")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
