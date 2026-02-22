package com.flaviorlf.lab.producer.messaging;

import com.flaviorlf.lab.common.model.EventMessage;

public interface EventPublisher {
    void publish(EventMessage message);
}
