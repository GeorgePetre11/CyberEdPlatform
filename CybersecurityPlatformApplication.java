// DESIGN PATTERN: FACTORY METHOD PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/CybersecurityPlatformApplication.java
//
// Description: Uses Spring's @Bean annotation to create factory methods. Each @Bean
// method acts as a factory that creates and configures objects with specific initialization logic.
//
// Pattern Elements:
// - Factory Methods: createAdmin(), seedCourses(), seedChallenges()
// - Products: User (admin), Course instances, Challenge instances
// - Creator: Spring IoC Container
// - Benefit: Centralizes object creation and initialization logic

package org.example.cybersecurity_platform;

import org.example.cybersecurity_platform.model.Challenge;
import org.example.cybersecurity_platform.model.Course;
import org.example.cybersecurity_platform.model.Role;
import org.example.cybersecurity_platform.model.User;
import org.example.cybersecurity_platform.repository.CourseRepository;
import org.example.cybersecurity_platform.repository.UserRepository;
import org.example.cybersecurity_platform.repository.ChallengeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class CybersecurityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CybersecurityPlatformApplication.class, args);
    }

    /**
     * FACTORY METHOD PATTERN: Creates admin user with specific configuration
     * 
     * This factory method encapsulates the logic for creating an admin user:
     * - Checks if admin already exists
     * - Creates new User instance
     * - Configures username, password, and roles
     * - Persists to database
     * 
     * Benefits:
     * - Centralizes admin creation logic
     * - Ensures consistent admin user configuration
     * - Can be easily modified or extended
     */
    @Bean
    CommandLineRunner createAdmin(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                // Factory creates and configures the admin User object
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("changeMe123"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN));
                userRepo.save(admin);
                System.out.println("🔐 Created default admin/changeMe123 — change the password ASAP");
            }
        };
    }

    /**
     * FACTORY METHOD PATTERN: Creates initial course instances
     * 
     * This factory method creates and configures multiple Course objects
     * with predefined values. It encapsulates the creation logic and
     * ensures courses are only created once.
     * 
     * Benefits:
     * - Centralizes course seeding logic
     * - Easy to add/modify initial courses
     * - Consistent course object creation
     */
    @Bean
    public CommandLineRunner seedCourses(CourseRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // Factory creates multiple Course instances with specific configurations
                repo.save(new Course("Intro to Cybersecurity",
                        "A first look at threats, defenses & best practices.",
                        49.99, 20));
                repo.save(new Course("Web App Pentesting",
                        "Hands-on hacking of web applications.",
                        99.00, 10));
                repo.save(new Course("Secure Coding in Java",
                        "Avoid the OWASP Top 10 via secure patterns.",
                        79.50, 15));
            }
        };
    }

    /**
     * FACTORY METHOD PATTERN: Creates initial challenge instances
     * 
     * This factory method creates Challenge objects with specific
     * configurations. It demonstrates how factory methods can create
     * different types of objects with varying parameters.
     * 
     * Benefits:
     * - Centralizes challenge initialization
     * - Easy to add new challenge types
     * - Encapsulates creation complexity
     */
    @Bean
    CommandLineRunner seedChallenges(ChallengeRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // Factory creates Challenge instances with different time limits
                repo.save(new Challenge(null, "Networking Beginner Challenge", 10));
                repo.save(new Challenge(null, "Networking Advanced Challenge", 5));
            }
        };
    }
}
