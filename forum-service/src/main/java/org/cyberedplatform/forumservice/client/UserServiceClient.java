package org.cyberedplatform.forumservice.client;

import org.cyberedplatform.forumservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserDTO getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            System.err.println("Failed to call User Service: " + e.getMessage());
            throw new RuntimeException("User service unavailable or user not found");
        }
    }
}
