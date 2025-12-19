package org.cyberedplatform.orderservice.controller;

import org.cyberedplatform.orderservice.model.Purchase;
import org.cyberedplatform.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void checkout_Success() throws Exception {
        // Arrange
        Purchase purchase = new Purchase(1L, 1L, "Test Course", 49.99, "COMPLETED");
        purchase.setId(1L);
        when(orderService.processCheckout(1L, 1L)).thenReturn(purchase);

        // Act & Assert
        mockMvc.perform(post("/api/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"courseId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseName").value("Test Course"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void getOrderById_Found() throws Exception {
        // Arrange
        Purchase purchase = new Purchase(1L, 1L, "Test Course", 49.99, "COMPLETED");
        purchase.setId(1L);
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(purchase));

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("Test Course"));
    }

    @Test
    void getOrderById_NotFound() throws Exception {
        // Arrange
        when(orderService.getOrderById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllOrders_ReturnsListOfOrders() throws Exception {
        // Arrange
        Purchase order1 = new Purchase(1L, 1L, "Course 1", 49.99, "COMPLETED");
        Purchase order2 = new Purchase(1L, 2L, "Course 2", 79.99, "COMPLETED");
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void health_ReturnsUp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/orders/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("order-service"));
    }
}
