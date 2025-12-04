package org.cyberedplatform.forumservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData() {
        return args -> {
            System.out.println("\n🔧 Initializing Forum Service...");
            System.out.println("✅ Forum Service initialized successfully\n");
        };
    }
}
