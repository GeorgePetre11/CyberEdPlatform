package org.cyberedplatform.courseservice.controller;

import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void getAllCourses_ReturnsListOfCourses() throws Exception {
        // Arrange
        Course course1 = new Course("Course 1", "Description 1", 49.99, 100);
        Course course2 = new Course("Course 2", "Description 2", 79.99, 50);
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        // Act & Assert
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Course 1"))
                .andExpect(jsonPath("$[1].title").value("Course 2"));
    }

    @Test
    void getCourseById_Found() throws Exception {
        // Arrange
        Course course = new Course("Test Course", "Description", 49.99, 100);
        course.setId(1L);
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        // Act & Assert
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Course"))
                .andExpect(jsonPath("$.price").value(49.99));
    }

    @Test
    void getCourseById_NotFound() throws Exception {
        // Arrange
        when(courseService.getCourseById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/courses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourse_Success() throws Exception {
        // Arrange
        Course course = new Course("New Course", "Description", 59.99, 50);
        course.setId(1L);
        when(courseService.createCourse(anyString(), anyString(), anyDouble(), anyInt()))
                .thenReturn(course);

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Course\",\"description\":\"Description\",\"price\":59.99,\"quantity\":50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Course"));
    }

    @Test
    void health_ReturnsUp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/courses/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("course-service"));
    }
}
