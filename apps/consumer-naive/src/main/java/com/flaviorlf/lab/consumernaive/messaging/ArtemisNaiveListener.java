package com.flaviorlf.lab.consumernaive.messaging;

import com.flaviorlf.lab.common.model.EventMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("artemis")
public class ArtemisNaiveListener {

    private final NaiveMessageHandler handler;

    public ArtemisNaiveListener(NaiveMessageHandler handler) {
        this.handler = handler;
    }

    @JmsListener(destination = "${lab.artemis.destination}")
    public void onMessage(EventMessage payload) {
        handler.handle(payload);
    }
}
