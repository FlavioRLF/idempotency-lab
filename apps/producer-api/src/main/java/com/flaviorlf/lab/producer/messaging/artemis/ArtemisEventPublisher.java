package com.flaviorlf.lab.producer.messaging.artemis;

import com.flaviorlf.lab.common.model.EventMessage;
import com.flaviorlf.lab.producer.messaging.EventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Profile("artemis")
public class ArtemisEventPublisher implements EventPublisher {

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public ArtemisEventPublisher(JmsTemplate jmsTemplate,
                                 @Value("${lab.artemis.destination}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @Override
    public void publish(EventMessage message) {
        jmsTemplate.convertAndSend(destination, message);
    }
}
