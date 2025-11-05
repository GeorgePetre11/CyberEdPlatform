// DESIGN PATTERN: OBSERVER PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/model/Purchase.java
//
// Description: Uses JPA/Hibernate's @CreationTimestamp annotation which implements
// the Observer pattern. The annotation observes entity lifecycle events and automatically
// sets the timestamp when the entity is persisted.
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
@Table(name = "purchases")
public class Purchase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Course course;

    /**
     * OBSERVER PATTERN: @CreationTimestamp observes entity lifecycle
     * 
     * When this entity is persisted to the database, the @CreationTimestamp
     * annotation automatically sets this field to the current timestamp.
     * 
     * The developer doesn't need to manually set this field - the JPA/Hibernate
     * observer pattern implementation handles it automatically.
     * 
     * Similar to: Subject.addObserver(CreationTimestampObserver)
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant purchasedAt;

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public Instant getPurchasedAt() {
        return purchasedAt;
    }

    // Setters (note: no setter for purchasedAt - it's managed by the observer)
    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // No setter for purchasedAt - the @CreationTimestamp observer sets it automatically
}
