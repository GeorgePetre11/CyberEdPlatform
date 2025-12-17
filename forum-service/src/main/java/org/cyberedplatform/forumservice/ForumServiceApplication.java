package org.cyberedplatform.forumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ForumServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Forum Service is running on port 8084");
        System.out.println("========================================\n");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
