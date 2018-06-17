package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.core.StringExpression;

/**
 * Пользовательское поле cf_environment (окружение воспроизведения ошибки)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketCFEnvironmentExpression extends StringExpression {
    public TicketCFEnvironmentExpression() {
        super("cf_environment");
    }

}
