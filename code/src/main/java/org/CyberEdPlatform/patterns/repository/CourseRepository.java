package org.CyberEdPlatform.patterns.repository;

import org.CyberEdPlatform.patterns.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
}
