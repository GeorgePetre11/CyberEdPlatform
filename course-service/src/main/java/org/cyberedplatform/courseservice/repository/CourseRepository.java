package org.cyberedplatform.courseservice.repository;

import org.cyberedplatform.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);
    boolean existsByTitle(String title);
}
