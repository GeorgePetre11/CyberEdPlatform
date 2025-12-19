package org.cyberedplatform.orderservice.service;

import org.cyberedplatform.orderservice.client.ServiceClient;
import org.cyberedplatform.orderservice.dto.CourseDTO;
import org.cyberedplatform.orderservice.dto.UserDTO;
import org.cyberedplatform.orderservice.model.Purchase;
import org.cyberedplatform.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ServiceClient serviceClient;

    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private OrderService orderService;

    private UserDTO testUser;
    private CourseDTO testCourse;
    private Purchase testPurchase;

    @BeforeEach
    void setUp() {
        testUser = new UserDTO();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testCourse = new CourseDTO();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setPrice(49.99);
        testCourse.setQuantity(10);

        testPurchase = new Purchase();
        testPurchase.setId(1L);
        testPurchase.setUserId(1L);
        testPurchase.setCourseId(1L);
        testPurchase.setCourseName("Test Course");
        testPurchase.setTotalPrice(49.99);
        testPurchase.setStatus("COMPLETED");
    }

    @Test
    void processCheckout_Success() {
        // Arrange
        when(serviceClient.getUserById(1L)).thenReturn(testUser);
        when(serviceClient.getCourseById(1L)).thenReturn(testCourse);
        when(orderRepository.save(any(Purchase.class))).thenReturn(testPurchase);

        // Act
        Purchase result = orderService.processCheckout(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Course", result.getCourseName());
        assertEquals("COMPLETED", result.getStatus());
        verify(orderRepository).save(any(Purchase.class));
        verify(messagePublisher).publishOrderPlacedEvent(any());
    }

    @Test
    void processCheckout_UserNotFound_ThrowsException() {
        // Arrange
        when(serviceClient.getUserById(1L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processCheckout(1L, 1L);
        });
        assertEquals("User not found", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void processCheckout_CourseNotFound_ThrowsException() {
        // Arrange
        when(serviceClient.getUserById(1L)).thenReturn(testUser);
        when(serviceClient.getCourseById(1L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processCheckout(1L, 1L);
        });
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void processCheckout_CourseOutOfStock_ThrowsException() {
        // Arrange
        testCourse.setQuantity(0);
        when(serviceClient.getUserById(1L)).thenReturn(testUser);
        when(serviceClient.getCourseById(1L)).thenReturn(testCourse);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.processCheckout(1L, 1L);
        });
        assertEquals("Course out of stock", exception.getMessage());
    }

    @Test
    void getOrderById_Found() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testPurchase));

        // Act
        Optional<Purchase> result = orderService.getOrderById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Course", result.get().getCourseName());
    }

    @Test
    void getOrderById_NotFound() {
        // Arrange
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Purchase> result = orderService.getOrderById(99L);

        // Assert
        assertFalse(result.isPresent());
    }
}
