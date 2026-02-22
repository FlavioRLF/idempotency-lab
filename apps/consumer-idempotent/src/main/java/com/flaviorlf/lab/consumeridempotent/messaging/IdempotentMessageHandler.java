package com.flaviorlf.lab.consumeridempotent.messaging;

import com.flaviorlf.lab.common.model.EventMessage;
import com.flaviorlf.lab.consumeridempotent.fail.FailMode;
import com.flaviorlf.lab.consumeridempotent.fail.FailureInjector;
import com.flaviorlf.lab.consumeridempotent.persistence.IdempotencyRepository;
import com.flaviorlf.lab.consumeridempotent.persistence.SideEffectsRepository;
import org.springframework.stereotype.Component;

@Component
public class IdempotentMessageHandler {

    private final SideEffectsRepository sideEffects;
    private final IdempotencyRepository idempotency;
    private final FailureInjector failure;

    public IdempotentMessageHandler(SideEffectsRepository sideEffects,
                                    IdempotencyRepository idempotency,
                                    FailureInjector failure) {
        this.sideEffects = sideEffects;
        this.idempotency = idempotency;
        this.failure = failure;
    }

    public void handle(EventMessage msg) {
        // First step: idempotency guard
        boolean firstTime = idempotency.tryMarkProcessed(msg.messageId());
        if (!firstTime) {
            // Already processed => skip side effect
            System.out.println("[IDEMPOTENT] Skipping duplicate messageId=" + msg.messageId());
            return;
        }

        // Optional delay
        if (failure.mode() == FailMode.SLEEP_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            try { Thread.sleep(Math.max(0, failure.sleepMs())); } catch (InterruptedException ignored) { }
        }

        // Persist side effect exactly once (even if redelivered)
        sideEffects.insertSideEffect(msg);

        // Fail after DB write but before ack => redelivery should occur, but skip due to processed_messages
        if (failure.mode() == FailMode.CRASH_AFTER_DB_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            System.err.println("[FAIL] CRASH_AFTER_DB_BEFORE_ACK triggered. Exiting JVM...");
            System.exit(2);
        }

        // Fail before ack (throw) => redelivery should occur, but skip due to processed_messages
        if (failure.mode() == FailMode.THROW_BEFORE_ACK && failure.shouldFail(msg.messageId())) {
            throw new RuntimeException("[FAIL] THROW_BEFORE_ACK triggered");
        }
    }
}
