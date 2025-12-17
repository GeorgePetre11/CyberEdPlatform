package org.cyberedplatform.courseservice.service;

import org.cyberedplatform.courseservice.config.RabbitMQConfig;
import org.cyberedplatform.courseservice.event.OrderPlacedEvent;
import org.cyberedplatform.courseservice.model.Course;
import org.cyberedplatform.courseservice.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Autowired
    private CourseRepository courseRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    @Transactional
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        logger.info("Received order placed event: {}", event);

        try {
            Course course = courseRepository.findById(event.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + event.getCourseId()));

            int newQuantity = course.getQuantity() + event.getQuantityChange();
            
            if (newQuantity < 0) {
                logger.error("Cannot update inventory: would result in negative quantity for course {}", event.getCourseId());
                return;
            }

            course.setQuantity(newQuantity);
            courseRepository.save(course);

            logger.info("Successfully updated course {} inventory. New quantity: {}", 
                    event.getCourseId(), newQuantity);

        } catch (Exception e) {
            logger.error("Error processing order placed event: {}", e.getMessage(), e);
            // In production, you would implement retry logic or dead letter queue
        }
    }
}
