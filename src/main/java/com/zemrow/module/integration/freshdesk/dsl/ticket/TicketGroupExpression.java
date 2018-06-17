package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.TicketGroup;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.NumberExpression;

/**
 * Поле group_id (группа)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketGroupExpression extends NumberExpression {
    public TicketGroupExpression() {
        super("group_id");
    }

    public BooleanExpression eq(TicketGroup status) {
        return eq(status.getCode());
    }
}
