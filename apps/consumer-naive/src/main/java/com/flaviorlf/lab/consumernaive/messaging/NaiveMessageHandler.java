package com.flaviorlf.lab.consumernaive.messaging;

import com.flaviorlf.lab.common.model.EventMessage;
import com.flaviorlf.lab.consumernaive.fail.FailMode;
import com.flaviorlf.lab.consumernaive.fail.FailureInjector;
import com.flaviorlf.lab.consumernaive.persistence.SideEffectsRepository;
import org.springframework.stereotype.Component;

@Component
public class NaiveMessageHandler {

    private final SideEffectsRepository repo;
    private final FailureInjector failure;

    public NaiveMessageHandler(SideEffectsRepository repo, FailureInjector failure) {
        this.repo = repo;
        this.failure = failure;
    }

    public void handle(EventMessage msg) {
        // Optional delay
        if (failure.mode() == FailMode.SLEEP_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            try { Thread.sleep(Math.max(0, failure.sleepMs())); } catch (InterruptedException ignored) { }
        }

        // Persist side effect (this is what will duplicate when redelivery occurs)
        repo.insertSideEffect(msg);

        // Fail after DB write but before ack => redelivery should cause duplicates
        if (failure.mode() == FailMode.CRASH_AFTER_DB_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            System.err.println("[FAIL] CRASH_AFTER_DB_BEFORE_ACK triggered. Exiting JVM...");
            System.exit(2);
        }

        // Fail before ack (throw) => redelivery
        if (failure.mode() == FailMode.THROW_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            throw new RuntimeException("[FAIL] THROW_BEFORE_ACK triggered");
        }
    }
}
