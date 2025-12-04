package org.cyberedplatform.forumservice.client;

import org.cyberedplatform.forumservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Client for calling User Service
 * Demonstrates inter-service communication
 */
@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    /**
     * Call User Service to get user by ID
     */
    public UserDTO getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            System.out.println("📞 Calling User Service: " + url);
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            System.err.println("❌ Failed to call User Service: " + e.getMessage());
            throw new RuntimeException("User service unavailable or user not found");
        }
    }
}
