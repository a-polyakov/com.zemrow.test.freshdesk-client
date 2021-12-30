package com.zemrow.module.integration.freshdesk.dsl.dto;

/**
 * Набор констант для поля group_id (группа)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
@Deprecated
public enum TicketGroup {
    TEST_GROUP(1L);

    private final long code;

    TicketGroup(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
