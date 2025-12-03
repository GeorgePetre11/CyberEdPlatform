package org.cyberedplatform.orderservice.client;

import org.cyberedplatform.orderservice.dto.CourseDTO;
import org.cyberedplatform.orderservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${course.service.url}")
    private String courseServiceUrl;

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

    /**
     * Call Course Service to get course by ID
     */
    public CourseDTO getCourseById(Long courseId) {
        try {
            String url = courseServiceUrl + "/api/courses/" + courseId;
            System.out.println("📞 Calling Course Service: " + url);
            return restTemplate.getForObject(url, CourseDTO.class);
        } catch (Exception e) {
            System.err.println("❌ Failed to call Course Service: " + e.getMessage());
            throw new RuntimeException("Course service unavailable or course not found");
        }
    }

    /**
     * Call Course Service to update inventory (decrease by 1)
     */
    public void updateCourseInventory(Long courseId, int quantityChange) {
        try {
            String url = courseServiceUrl + "/api/courses/" + courseId + "/inventory";
            System.out.println("📞 Calling Course Service to update inventory: " + url);
            
            Map<String, Integer> request = new HashMap<>();
            request.put("quantityChange", quantityChange);
            
            restTemplate.put(url, request);
            System.out.println("✅ Inventory updated successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to update inventory: " + e.getMessage());
            throw new RuntimeException("Failed to update course inventory: " + e.getMessage());
        }
    }
}
