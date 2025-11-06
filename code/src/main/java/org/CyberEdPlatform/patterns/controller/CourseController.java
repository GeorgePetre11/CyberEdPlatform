package org.CyberEdPlatform.patterns.controller;

import org.CyberEdPlatform.patterns.model.Course;
import org.CyberEdPlatform.patterns.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        System.out.println("[SINGLETON] CourseController instance created");
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        System.out.println("[SINGLETON] CourseController listing courses");
        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }
}
