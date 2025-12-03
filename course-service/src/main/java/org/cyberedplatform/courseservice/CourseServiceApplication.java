package org.cyberedplatform.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Course Service is running on port 8082");
        System.out.println("H2 Console: http://localhost:8082/h2-console");
        System.out.println("========================================\n");
    }
}
