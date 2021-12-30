package com.zemrow.module.integration.freshdesk.dsl.dto;

/**
 * Набор констант с идентификаторами пользователей
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
@Deprecated
public enum User {
    TEST_USER(1L);

    private final long code;

    User(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
