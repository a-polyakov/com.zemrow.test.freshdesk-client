package com.zemrow.module.integration.freshdesk.dsl.ticket;

import com.zemrow.module.integration.freshdesk.dsl.Company;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.dsl.core.NumberExpression;

/**
 * Поле company
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketCompanyExpression extends NumberExpression {
    public TicketCompanyExpression() {
        super("company");
    }

    public BooleanExpression eq(Company company) {
        return eq(company.getCode());
    }
}
