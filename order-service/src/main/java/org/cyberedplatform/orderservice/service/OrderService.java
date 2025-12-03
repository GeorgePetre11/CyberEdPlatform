package org.cyberedplatform.orderservice.service;

import org.cyberedplatform.orderservice.client.ServiceClient;
import org.cyberedplatform.orderservice.dto.CourseDTO;
import org.cyberedplatform.orderservice.dto.UserDTO;
import org.cyberedplatform.orderservice.model.Purchase;
import org.cyberedplatform.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceClient serviceClient;

    /**
     * Process checkout - creates order and updates inventory
     * Demonstrates inter-service communication
     */
    @Transactional
    public Purchase processCheckout(Long userId, Long courseId) {
        System.out.println("\n🛒 Processing checkout for user " + userId + " and course " + courseId);

        // Step 1: Validate user exists (call User Service)
        UserDTO user = serviceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        System.out.println("✅ User validated: " + user.getUsername());

        // Step 2: Get course details (call Course Service)
        CourseDTO course = serviceClient.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        System.out.println("✅ Course found: " + course.getTitle() + " ($" + course.getPrice() + ")");

        // Step 3: Check inventory
        if (course.getQuantity() <= 0) {
            throw new RuntimeException("Course out of stock");
        }

        // Step 4: Create purchase
        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setCourseId(courseId);
        purchase.setCourseName(course.getTitle());
        purchase.setTotalPrice(course.getPrice());
        purchase.setStatus("COMPLETED");
        
        Purchase savedPurchase = orderRepository.save(purchase);
        System.out.println("✅ Purchase created: #" + savedPurchase.getId());

        // Step 5: Update course inventory (call Course Service)
        serviceClient.updateCourseInventory(courseId, -1);
        System.out.println("✅ Inventory updated\n");

        return savedPurchase;
    }

    // Get purchase by ID
    public Optional<Purchase> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Get all purchases for a user
    public List<Purchase> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Get all purchases
    public List<Purchase> getAllOrders() {
        return orderRepository.findAll();
    }
}
