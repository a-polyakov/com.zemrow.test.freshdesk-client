package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.TicketType;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.StringExpression;
import com.zemrow.module.integration.freshdesk.dsl.json.TicketConst;

/**
 * Пользовательское поле type (тип задачи)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketTypeExpression extends StringExpression {
    public TicketTypeExpression() {
        super(TicketConst.TYPE);
    }

    public BooleanExpression eq(TicketType type) {
        return eq(type.getCode());
    }
}
