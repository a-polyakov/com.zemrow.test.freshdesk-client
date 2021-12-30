package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.NumberExpression;
import com.zemrow.module.integration.freshdesk.dsl.dto.User;
/**
 * Поле agent_id (исполнитель)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketAgentExpression extends NumberExpression {
    public TicketAgentExpression() {
        super("agent_id");
    }

    public BooleanExpression eq(User user) {
        return eq(user.getCode());
    }
}
