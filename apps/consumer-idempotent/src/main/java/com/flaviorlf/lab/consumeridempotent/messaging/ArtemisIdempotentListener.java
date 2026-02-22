package com.flaviorlf.lab.consumeridempotent.messaging;

import com.flaviorlf.lab.common.model.EventMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("artemis")
public class ArtemisIdempotentListener {

    private final IdempotentMessageHandler handler;

    public ArtemisIdempotentListener(IdempotentMessageHandler handler) {
        this.handler = handler;
    }

    @JmsListener(destination = "${lab.artemis.destination}")
    public void onMessage(EventMessage payload) {
        handler.handle(payload);
    }
}
