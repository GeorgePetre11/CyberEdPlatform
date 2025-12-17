package org.cyberedplatform.orderservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

/**
 * Purchase Model - Represents a course purchase
 * Demonstrates Observer Pattern through @CreationTimestamp annotation
 */
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String status; // PENDING, COMPLETED, FAILED

    // Observer Pattern: @CreationTimestamp automatically populates this field
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant purchasedAt;

    public Purchase() {}

    public Purchase(Long userId, Long courseId, String courseName, double totalPrice, String status) {
        this.userId = userId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(Instant purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
