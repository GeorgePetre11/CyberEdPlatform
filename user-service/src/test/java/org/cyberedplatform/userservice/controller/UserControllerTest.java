package org.cyberedplatform.userservice.controller;

import org.cyberedplatform.userservice.dto.RegisterRequest;
import org.cyberedplatform.userservice.dto.UserDTO;
import org.cyberedplatform.userservice.model.Role;
import org.cyberedplatform.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void register_Success() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "testuser", Set.of(Role.ROLE_USER));
        when(userService.registerUser(anyString(), anyString())).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserById_Found() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "testuser", Set.of(Role.ROLE_USER));
        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        // Arrange
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void health_ReturnsUp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("user-service"));
    }
}
