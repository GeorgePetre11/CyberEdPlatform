package org.cyberedplatform.courseservice.config;

import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(CourseRepository courseRepository) {
        return args -> {
            System.out.println("\n🔧 Initializing Course Service data...");
            
            if (courseRepository.count() == 0) {
                courseRepository.save(new Course(
                        "Introduction to Cybersecurity",
                        "Learn the basics of cybersecurity including threats, vulnerabilities, and defenses",
                        49.99,
                        100
                ));
                
                courseRepository.save(new Course(
                        "Ethical Hacking Fundamentals",
                        "Master ethical hacking techniques and penetration testing methodologies",
                        79.99,
                        50
                ));
                
                courseRepository.save(new Course(
                        "Network Security",
                        "Secure networks against attacks with firewalls, VPNs, and IDS/IPS",
                        59.99,
                        75
                ));
                
                courseRepository.save(new Course(
                        "Web Application Security",
                        "Identify and prevent OWASP Top 10 vulnerabilities in web applications",
                        69.99,
                        60
                ));
                
                System.out.println("✅ Sample courses created");
            }
            
            System.out.println("✅ Course Service initialized successfully\n");
        };
    }
}
