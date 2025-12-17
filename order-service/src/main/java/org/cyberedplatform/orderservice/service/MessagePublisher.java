package org.cyberedplatform.orderservice.service;

import org.cyberedplatform.orderservice.config.RabbitMQConfig;
import org.cyberedplatform.orderservice.event.OrderPlacedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishOrderPlacedEvent(OrderPlacedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                event
        );
    }
}
