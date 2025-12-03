package org.cyberedplatform.courseservice.service;

import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Create course
    @Transactional
    public Course createCourse(String title, String description, double price, int quantity) {
        if (courseRepository.existsByTitle(title)) {
            throw new RuntimeException("Course with this title already exists");
        }

        Course course = new Course(title, description, price, quantity);
        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Update course
    @Transactional
    public Course updateCourse(Long id, String title, String description, double price, int quantity) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (title != null) course.setTitle(title);
        if (description != null) course.setDescription(description);
        if (price > 0) course.setPrice(price);
        if (quantity >= 0) course.setQuantity(quantity);

        return courseRepository.save(course);
    }

    // Delete course
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    // Update inventory (for Order Service)
    @Transactional
    public Course updateInventory(Long id, int quantityChange) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int newQuantity = course.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient inventory");
        }

        course.setQuantity(newQuantity);
        return courseRepository.save(course);
    }
}
