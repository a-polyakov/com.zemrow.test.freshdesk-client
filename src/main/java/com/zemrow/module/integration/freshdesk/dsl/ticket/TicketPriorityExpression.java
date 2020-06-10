package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.TicketPriority;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.NumberExpression;
import com.zemrow.module.integration.freshdesk.dsl.json.TicketConst;

/**
 * Поле priority (приоритет)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketPriorityExpression extends NumberExpression {
    public TicketPriorityExpression() {
        super(TicketConst.PRIORITY);
    }

    public BooleanExpression eq(TicketPriority priority) {
        return eq(priority.getCode());
    }
}
