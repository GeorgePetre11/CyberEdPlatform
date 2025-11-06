package org.CyberEdPlatform.patterns;

import org.CyberEdPlatform.patterns.model.Course;
import org.CyberEdPlatform.patterns.model.Role;
import org.CyberEdPlatform.patterns.model.User;
import org.CyberEdPlatform.patterns.repository.CourseRepository;
import org.CyberEdPlatform.patterns.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class DesignPatternsDemoApplication {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("       Design Patterns Demo Application Starting...        ");
        System.out.println("============================================================");
        SpringApplication.run(DesignPatternsDemoApplication.class, args);
        System.out.println("\nApplication started successfully!");
        System.out.println("Visit: http://localhost:8080");
        System.out.println("Login with: admin / demo123\n");
    }

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            System.out.println("\n[FACTORY METHOD] Creating admin user...");
            
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("demo123"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
                userRepo.save(admin);
                
                System.out.println("[FACTORY METHOD] Admin user created successfully!");
                System.out.println("   Username: admin");
                System.out.println("   Password: demo123");
            } else {
                System.out.println("[FACTORY METHOD] Admin user already exists");
            }
        };
    }

    @Bean
    CommandLineRunner seedCourses(CourseRepository courseRepo) {
        return args -> {
            System.out.println("\n[FACTORY METHOD] Seeding courses...");
            
            if (courseRepo.count() == 0) {
                courseRepo.save(new Course(
                    "Intro to Cybersecurity",
                    "Learn the fundamentals of cybersecurity, including threats, defenses, and best practices.",
                    49.99,
                    20
                ));
                
                courseRepo.save(new Course(
                    "Web Application Pentesting",
                    "Hands-on course about penetration testing web applications. Learn OWASP Top 10 vulnerabilities.",
                    99.00,
                    10
                ));
                
                courseRepo.save(new Course(
                    "Secure Coding in Java",
                    "Master secure coding practices in Java. Avoid common vulnerabilities and write safer code.",
                    79.50,
                    15
                ));
                
                System.out.println("[FACTORY METHOD] Seeded 3 courses successfully!");
                System.out.println("   Notice: @CreationTimestamp (Observer Pattern) auto-set timestamps");
            } else {
                System.out.println("[FACTORY METHOD] Courses already exist");
            }
        };
    }

    
    @Bean
    CommandLineRunner createDemoUser(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            System.out.println("\n[FACTORY METHOD] Creating demo user...");
            
            if (userRepo.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setRoles(Set.of(Role.ROLE_USER));
                userRepo.save(user);
                
                System.out.println("[FACTORY METHOD] Demo user created successfully!");
                System.out.println("   Username: user");
                System.out.println("   Password: user123");
            } else {
                System.out.println("[FACTORY METHOD] Demo user already exists");
            }
            
            System.out.println("\n============================================================");
            System.out.println("            All Design Patterns Initialized!               ");
            System.out.println("============================================================");
            System.out.println("  Facade Pattern       - CartService                      ");
            System.out.println("  Strategy Pattern     - PasswordEncoder (BCrypt)         ");
            System.out.println("  Observer Pattern     - @CreationTimestamp on entities   ");
            System.out.println("  Factory Method       - @Bean methods above              ");
            System.out.println("  Template Method      - CustomUserDetailsService         ");
            System.out.println("  Singleton Pattern    - All Spring-managed beans         ");
            System.out.println("============================================================");
        };
    }
}
