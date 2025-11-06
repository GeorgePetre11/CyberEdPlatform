package org.CyberEdPlatform.patterns.repository;

import org.CyberEdPlatform.patterns.model.Purchase;
import org.CyberEdPlatform.patterns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);
}
