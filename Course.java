// DESIGN PATTERN: OBSERVER PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/model/Course.java
//
// Description: Uses JPA/Hibernate's @CreationTimestamp annotation which implements
// the Observer pattern. The annotation observes entity lifecycle events and automatically
// sets the timestamp when the entity is created.
//
// Pattern Elements:
// - Subject: JPA/Hibernate EntityManager
// - Observer: @CreationTimestamp annotation processor
// - Event: Entity persist/insert event
// - Notification: Automatic timestamp setting

package org.example.cybersecurity_platform.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    /**
     * OBSERVER PATTERN: @CreationTimestamp observes entity lifecycle
     * 
     * This field is automatically populated when the Course entity is first
     * persisted to the database. The developer never needs to set this value
     * manually - the JPA/Hibernate observer mechanism handles it.
     * 
     * This demonstrates the Observer pattern where:
     * - The EntityManager is the Subject
     * - @CreationTimestamp annotation marks this field to be observed
     * - When entity is persisted (event), timestamp is set (notification)
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // --- Constructors ---
    public Course() {}

    public Course(String title, String description, double price, int quantity) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        // Note: createdAt is NOT set here - the Observer pattern handles it
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // No setter for createdAt - managed by @CreationTimestamp observer
}
