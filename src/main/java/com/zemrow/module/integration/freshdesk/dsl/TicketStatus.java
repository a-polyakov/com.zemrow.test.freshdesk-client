package com.zemrow.module.integration.freshdesk.dsl;

/**
 * Набор констант для поле status (статус)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public enum TicketStatus {
    Open(2),
    Analysis(9),
    Pending(3),
    Resolved(4),
    Closed(5),
    WaitingOnCustomer(6);

    private final int code;

    TicketStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TicketStatus valueOf(int code) {
        switch (code) {
            case 2:
                return Open;
            case 9:
                return Analysis;
            case 3:
                return Pending;
            case 4:
                return Resolved;
            case 5:
                return Closed;
            case 6:
                return WaitingOnCustomer;
            default:
                return null;
        }
    }
}
