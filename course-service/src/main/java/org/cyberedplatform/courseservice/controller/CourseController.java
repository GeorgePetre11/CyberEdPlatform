package org.cyberedplatform.courseservice.controller;

import org.cyberedplatform.courseservice.dto.CourseRequest;
import org.cyberedplatform.courseservice.dto.InventoryUpdateRequest;
import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest request) {
        try {
            Course course = courseService.createCourse(
                    request.getTitle(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getQuantity()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) {
        try {
            Course course = courseService.updateCourse(
                    id,
                    request.getTitle(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getQuantity()
            );
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody InventoryUpdateRequest request) {
        try {
            Course course = courseService.updateInventory(id, request.getQuantityChange());
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "course-service"));
    }
}
