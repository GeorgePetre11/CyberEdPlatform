package org.cyberedplatform.courseservice.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderPlacedEvent implements Serializable {
    private Long orderId;
    private Long userId;
    private Long courseId;
    private String courseName;
    private Double totalPrice;
    private LocalDateTime timestamp;
    private Integer quantityChange; // negative value to decrease inventory

    public OrderPlacedEvent() {}

    public OrderPlacedEvent(Long orderId, Long userId, Long courseId, String courseName, 
                           Double totalPrice, LocalDateTime timestamp, Integer quantityChange) {
        this.orderId = orderId;
        this.userId = userId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.quantityChange = quantityChange;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Integer getQuantityChange() { return quantityChange; }
    public void setQuantityChange(Integer quantityChange) { this.quantityChange = quantityChange; }
}
