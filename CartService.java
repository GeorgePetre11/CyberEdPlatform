// DESIGN PATTERN: FACADE PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/service/CartService.java
// 
// Description: This class provides a simplified interface for cart operations,
// hiding the complexity of working with repositories and data transformations.
//
// Pattern Elements:
// - Facade: CartService provides simple methods (add, remove, clear, getTotalPrice)
// - Subsystem: CourseRepository, Map operations, Stream API
// - Client: Controllers and other services use simple cart methods without worrying about implementation

package org.example.cybersecurity_platform.service;

import org.example.cybersecurity_platform.model.Course;
import org.example.cybersecurity_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.AbstractMap;
import java.util.stream.Collectors;

@Service
@SessionScope
public class CartService {

    private final CourseRepository courseRepo;
    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public CartService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    /**
     * Returns a map of Course → quantity for the items in the cart.
     * FACADE: Simplifies complex repository lookup and stream operations
     */
    public Map<Course, Integer> getItems() {
        return items.entrySet().stream()
                .map(e -> {
                    return courseRepo.findById(e.getKey())
                            .map(c -> new AbstractMap.SimpleEntry<>(c, e.getValue()))
                            .orElse(null);
                })
                .filter(entry -> entry != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * Add one unit of the given course to the cart.
     * FACADE: Simple interface hiding Map.merge complexity
     */
    public void add(Long courseId) {
        items.merge(courseId, 1, Integer::sum);
    }

    /**
     * Remove the given course entirely from the cart.
     * FACADE: Simple method for cart item removal
     */
    public void remove(Long courseId) {
        items.remove(courseId);
    }

    /**
     * Clear all items from the cart.
     * FACADE: Simple method to reset cart
     */
    public void clear() {
        items.clear();
    }

    /**
     * Returns true if the cart has no items.
     * FACADE: Simple boolean check
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Total quantity of all items in the cart.
     * FACADE: Hides stream reduction complexity
     */
    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Total price of all items in the cart.
     * FACADE: Hides complex calculation of total price across multiple courses
     */
    public double getTotalPrice() {
        return getItems().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }
}
