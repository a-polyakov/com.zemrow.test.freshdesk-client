package com.zemrow.module.integration.freshdesk.dsl;

import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketAgentExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketCFActionExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketCFEnvironmentExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketCompanyExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketGroupExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketPriorityExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketStatusExpression;
import com.zemrow.module.integration.freshdesk.dsl.ticket.TicketTypeExpression;

/**
 * Программная обертка для формирования запроса по задачам
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class TicketDsl {
    /**
     * Группа
     */
    public static final TicketGroupExpression group_id = new TicketGroupExpression();
    /**
     * Приоритет
     */
    public static final TicketPriorityExpression priority = new TicketPriorityExpression();
    /**
     * Компания
     */
    public static final TicketCompanyExpression company_id = new TicketCompanyExpression();
    /**
     * Статус
     */
    public static final TicketStatusExpression status = new TicketStatusExpression();
    /**
     * Исполнитель
     */
    public static final TicketAgentExpression agent_id = new TicketAgentExpression();
    /**
     * Тип задачи
     */
    public static final TicketTypeExpression type = new TicketTypeExpression();
    /**
     * Окружение воспроизведения ошибки
     */
    public static final TicketCFEnvironmentExpression cf_environment = new TicketCFEnvironmentExpression();
    /**
     * Ожидает починки
     */
    public static final TicketCFActionExpression action = new TicketCFActionExpression();
}
