package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поле group_id (группа)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
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
