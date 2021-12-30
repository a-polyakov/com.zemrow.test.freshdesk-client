package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.core.StringExpression;

/**
 * Пользовательское поле priority_internal (ожидает починки)
 *
 * @author Alexandr Polyakov on 2018.06.27
 */
public class TicketCFActionExpression extends StringExpression {
    public TicketCFActionExpression() {
        super("priority_internal");
    }

}
