package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поле type (тип ошибки)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public enum TicketType {
    Question("Question (clarification)"),
    Problem("Problem (general issue)"),
    Feature("Feature Request (new feature)"),
    Bug("Bug (broken functionality)");

    private final String code;

    TicketType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
