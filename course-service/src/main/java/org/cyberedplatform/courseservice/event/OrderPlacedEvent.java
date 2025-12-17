package org.cyberedplatform.courseservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent implements Serializable {
    private Long orderId;
    private Long userId;
    private Long courseId;
    private String courseName;
    private Double totalPrice;
    private LocalDateTime timestamp;
    private Integer quantityChange; // negative value to decrease inventory
}
