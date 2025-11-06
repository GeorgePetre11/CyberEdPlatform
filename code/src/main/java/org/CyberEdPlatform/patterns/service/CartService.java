package org.CyberEdPlatform.patterns.service;

import org.CyberEdPlatform.patterns.model.Course;
import org.CyberEdPlatform.patterns.repository.CourseRepository;
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

    public void add(Long courseId) {
        items.merge(courseId, 1, Integer::sum);
    }

    public void remove(Long courseId) {
        items.remove(courseId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getTotalPrice() {
        return getItems().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }
}
