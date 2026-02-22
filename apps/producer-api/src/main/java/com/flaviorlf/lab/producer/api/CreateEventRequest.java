package com.flaviorlf.lab.producer.api;

import java.math.BigDecimal;

public record CreateEventRequest(
        String customerId,
        BigDecimal amount
) {}
