package com.flaviorlf.lab.producer.api;

import com.flaviorlf.lab.common.model.EventMessage;
import com.flaviorlf.lab.producer.messaging.EventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventPublisher publisher;

    public EventController(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<EventMessage> publish(@RequestBody CreateEventRequest req) {

        EventMessage msg = new EventMessage(
                UUID.randomUUID(),
                "ORDER_CREATED",
                UUID.randomUUID(),
                req.customerId(),
                req.amount(),
                Instant.now()
        );

        publisher.publish(msg);
        return ResponseEntity.accepted().body(msg);
    }
}
