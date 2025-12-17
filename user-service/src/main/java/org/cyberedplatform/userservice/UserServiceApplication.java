package org.cyberedplatform.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("User Service is running on port 8081");
        System.out.println("H2 Console: http://localhost:8081/h2-console");
        System.out.println("========================================\n");
    }
}
