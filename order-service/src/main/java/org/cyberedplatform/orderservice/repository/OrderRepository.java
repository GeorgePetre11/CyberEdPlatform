package org.cyberedplatform.orderservice.repository;

import org.cyberedplatform.orderservice.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Long userId);
    List<Purchase> findByCourseId(Long courseId);
}
