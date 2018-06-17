package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поля company
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public enum Company {
    TEST_COMPANY(1L);

    private final long code;

    Company(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
