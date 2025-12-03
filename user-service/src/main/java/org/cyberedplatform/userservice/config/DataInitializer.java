package org.cyberedplatform.userservice.config;

import org.cyberedplatform.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserService userService) {
        return args -> {
            System.out.println("\n🔧 Initializing User Service data...");
            userService.createAdminUser();
            System.out.println("✅ User Service initialized successfully\n");
        };
    }
}
