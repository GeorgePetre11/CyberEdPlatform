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

    @Transactional
    public Purchase processCheckout(Long userId, Long courseId) {
        UserDTO user = serviceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        CourseDTO course = serviceClient.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        if (course.getQuantity() <= 0) {
            throw new RuntimeException("Course out of stock");
        }

        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setCourseId(courseId);
        purchase.setCourseName(course.getTitle());
        purchase.setTotalPrice(course.getPrice());
        purchase.setStatus("COMPLETED");
        
        Purchase savedPurchase = orderRepository.save(purchase);
        serviceClient.updateCourseInventory(courseId, -1);

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
