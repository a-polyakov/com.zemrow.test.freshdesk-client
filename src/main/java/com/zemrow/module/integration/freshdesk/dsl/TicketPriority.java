package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поле priority (приоритет)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public enum TicketPriority {
    Low(1),
    Medium(2),
    High(3),
    Urgent(4);

    private final int code;

    TicketPriority(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TicketPriority valueOf(int code) {
        switch (code) {
            case 1:
                return Low;
            case 2:
                return Medium;
            case 3:
                return High;
            case 4:
                return Urgent;
            default:
                return null;
        }
    }
}
