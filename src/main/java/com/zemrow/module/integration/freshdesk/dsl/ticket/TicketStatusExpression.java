package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.TicketStatus;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.NumberExpression;
/**
 * Поле status (статус)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketStatusExpression extends NumberExpression {
    public TicketStatusExpression() {
        super("status");
    }

    public BooleanExpression eq(TicketStatus status) {
        return eq(status.getCode());
    }
}
