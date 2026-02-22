package com.flaviorlf.lab.consumernaive.messaging;

import com.flaviorlf.lab.common.model.EventMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("rabbit")
public class RabbitNaiveListener {

    private final NaiveMessageHandler handler;

    public RabbitNaiveListener(NaiveMessageHandler handler) {
        this.handler = handler;
    }

    @RabbitListener(queues = "${lab.rabbit.queue}", ackMode = "MANUAL")
    public void onMessage(EventMessage payload, Message raw, Channel channel) throws Exception {
        long tag = raw.getMessageProperties().getDeliveryTag();
        try {
            handler.handle(payload);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // Requeue = true to force redelivery
            channel.basicNack(tag, false, true);
            throw e;
        }
    }
}
