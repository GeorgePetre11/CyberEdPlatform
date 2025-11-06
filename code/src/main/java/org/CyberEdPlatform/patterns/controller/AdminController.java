package org.CyberEdPlatform.patterns.controller;

import org.CyberEdPlatform.patterns.model.Course;
import org.CyberEdPlatform.patterns.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseRepository courseRepository;

    public AdminController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        System.out.println("[SINGLETON] AdminController instance created");
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        System.out.println("[SINGLETON] AdminController listing courses");
        model.addAttribute("courses", courseRepository.findAll());
        return "admin-courses";
    }

    @PostMapping("/courses/create")
    public String createCourse(@ModelAttribute Course course) {
        System.out.println("[SINGLETON] AdminController creating course");
        System.out.println("[OBSERVER] Course will be saved with auto-set createdAt timestamp");
        
        courseRepository.save(course);
        
        System.out.println("Course created: " + course.getTitle());
        System.out.println("   CreatedAt: " + course.getCreatedAt() + " (auto-set by Observer)");
        
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        System.out.println("[SINGLETON] AdminController deleting course");
        courseRepository.deleteById(id);
        return "redirect:/admin/courses";
    }
}
