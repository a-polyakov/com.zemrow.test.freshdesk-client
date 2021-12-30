package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поля type (тип ошибки)
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

    public static TicketType valueOfByCode(String code) {
        switch (code) {
            case "Question":
            case "Question (clarification)":
                return Question;
            case "Problem":
            case "Problem (general issue)":
            case "Incident":
                return Problem;
            case "Feature Request":
            case "Feature Request (new feature)":
                return Feature;
            case "Bug":
            case "Bug (broken functionality)":
                return Bug;
            default:
                return null;
        }
    }
}
