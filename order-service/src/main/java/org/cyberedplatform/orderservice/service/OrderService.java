package org.cyberedplatform.orderservice.service;

import org.cyberedplatform.orderservice.client.ServiceClient;
import org.cyberedplatform.orderservice.dto.CourseDTO;
import org.cyberedplatform.orderservice.dto.UserDTO;
import org.cyberedplatform.orderservice.event.OrderPlacedEvent;
import org.cyberedplatform.orderservice.model.Purchase;
import org.cyberedplatform.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceClient serviceClient;

    @Autowired
    private MessagePublisher messagePublisher;

    @Transactional
    public Purchase processCheckout(Long userId, Long courseId) {
        // Validate user exists
        UserDTO user = serviceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate course exists and check availability
        CourseDTO course = serviceClient.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        if (course.getQuantity() <= 0) {
            throw new RuntimeException("Course out of stock");
        }

        // Create and save purchase order
        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setCourseId(courseId);
        purchase.setCourseName(course.getTitle());
        purchase.setTotalPrice(course.getPrice());
        purchase.setStatus("COMPLETED");
        
        Purchase savedPurchase = orderRepository.save(purchase);

        // Publish async event to update inventory (replaces synchronous call)
        OrderPlacedEvent event = new OrderPlacedEvent(
                savedPurchase.getId(),
                userId,
                courseId,
                course.getTitle(),
                course.getPrice(),
                LocalDateTime.now(),
                -1  // decrease inventory by 1
        );
        
        messagePublisher.publishOrderPlacedEvent(event);
        logger.info("Published OrderPlacedEvent for order {} to RabbitMQ", savedPurchase.getId());

        return savedPurchase;
    }

    public Optional<Purchase> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Purchase> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Purchase> getAllOrders() {
        return orderRepository.findAll();
    }
}
