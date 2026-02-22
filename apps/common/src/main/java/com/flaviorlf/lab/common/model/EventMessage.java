package com.flaviorlf.lab.common.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventMessage(
        UUID messageId,
        String type,
        UUID orderId,
        String customerId,
        BigDecimal amount,
        Instant occurredAt
) {}
