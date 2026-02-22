package com.flaviorlf.lab.consumeridempotent.fail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FailureInjector {

    private final FailMode mode;
    private final double rate;
    private final long sleepMs;
    private final Set<UUID> failOnIds;
    private final Random random = new Random();

    public FailureInjector(
            @Value("${lab.fail.mode:NONE}") String mode,
            @Value("${lab.fail.rate:0.0}") double rate,
            @Value("${lab.fail.sleep-ms:0}") long sleepMs,
            @Value("${lab.fail.on-message-ids:}") String failOnMessageIds
    ) {
        this.mode = FailMode.valueOf(mode.trim().toUpperCase());
        this.rate = rate;
        this.sleepMs = sleepMs;
        this.failOnIds = parseIds(failOnMessageIds);
    }

    public FailMode mode() {
        return mode;
    }

    public long sleepMs() {
        return sleepMs;
    }

    public boolean shouldFail(UUID messageId) {
        if (mode == FailMode.NONE) return false;

        if (!failOnIds.isEmpty()) {
            return failOnIds.contains(messageId);
        }
        if (rate <= 0.0) return false;
        return random.nextDouble() < rate;
    }

    private Set<UUID> parseIds(String csv) {
        if (csv == null || csv.isBlank()) return Set.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }
}
