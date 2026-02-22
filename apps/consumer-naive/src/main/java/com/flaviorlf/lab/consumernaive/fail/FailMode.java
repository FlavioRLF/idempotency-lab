package com.flaviorlf.lab.consumernaive.fail;

public enum FailMode {
    NONE,
    THROW_BEFORE_ACK,
    CRASH_AFTER_DB_BEFORE_ACK,
    SLEEP_BEFORE_ACK
}
