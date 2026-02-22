package com.flaviorlf.lab.producer.messaging.rabbit;

import com.flaviorlf.lab.common.model.EventMessage;
import com.flaviorlf.lab.producer.messaging.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("rabbit")
public class RabbitEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${lab.rabbit.exchange}") String exchange,
            @Value("${lab.rabbit.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void publish(EventMessage message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
