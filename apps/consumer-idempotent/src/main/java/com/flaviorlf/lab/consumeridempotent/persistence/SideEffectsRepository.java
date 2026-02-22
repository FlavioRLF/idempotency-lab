package com.flaviorlf.lab.consumeridempotent.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flaviorlf.lab.common.model.EventMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SideEffectsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public SideEffectsRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void insertSideEffect(EventMessage msg) {
        try {
            String payloadJson = objectMapper.writeValueAsString(msg);
            jdbcTemplate.update(
                    "INSERT INTO side_effects (message_id, payload) VALUES (?, ?::jsonb)",
                    msg.messageId(),
                    payloadJson
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist side effect", e);
        }
    }
}
