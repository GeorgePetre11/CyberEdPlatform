package org.cyberedplatform.courseservice.service;

import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setPrice(49.99);
        testCourse.setQuantity(100);
    }

    @Test
    void createCourse_Success() {
        // Arrange
        when(courseRepository.existsByTitle(anyString())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.createCourse("Test Course", "Test Description", 49.99, 100);

        // Assert
        assertNotNull(result);
        assertEquals("Test Course", result.getTitle());
        assertEquals(100, result.getQuantity());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_TitleExists_ThrowsException() {
        // Arrange
        when(courseRepository.existsByTitle("Test Course")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.createCourse("Test Course", "Description", 49.99, 100);
        });
        assertEquals("Course with this title already exists", exception.getMessage());
    }

    @Test
    void getCourseById_Found() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Optional<Course> result = courseService.getCourseById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Course", result.get().getTitle());
    }

    @Test
    void updateInventory_Success() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.updateInventory(1L, -10);

        // Assert
        assertNotNull(result);
        assertEquals(90, result.getQuantity());
        verify(courseRepository).save(testCourse);
    }

    @Test
    void updateInventory_InsufficientInventory_ThrowsException() {
        // Arrange
        testCourse.setQuantity(5);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.updateInventory(1L, -10);
        });
        assertEquals("Insufficient inventory", exception.getMessage());
    }

    @Test
    void deleteCourse_Success() {
        // Arrange
        when(courseRepository.existsById(1L)).thenReturn(true);

        // Act
        courseService.deleteCourse(1L);

        // Assert
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void deleteCourse_NotFound_ThrowsException() {
        // Arrange
        when(courseRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.deleteCourse(99L);
        });
        assertEquals("Course not found", exception.getMessage());
    }
}
