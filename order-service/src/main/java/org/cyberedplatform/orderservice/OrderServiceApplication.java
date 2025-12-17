package org.cyberedplatform.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Order Service is running on port 8083");
        System.out.println("H2 Console: http://localhost:8083/h2-console");
        System.out.println("========================================\n");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
