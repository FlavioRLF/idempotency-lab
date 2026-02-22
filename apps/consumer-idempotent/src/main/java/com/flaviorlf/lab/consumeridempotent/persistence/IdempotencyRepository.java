package com.flaviorlf.lab.consumeridempotent.persistence;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class IdempotencyRepository {

    private final JdbcTemplate jdbcTemplate;

    public IdempotencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return true if this messageId is being processed for the first time; false if it was already processed.
     */
    public boolean tryMarkProcessed(UUID messageId) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO processed_messages (message_id) VALUES (?)",
                    messageId
            );
            return true;
        } catch (DuplicateKeyException dup) {
            return false;
        }
    }
}
