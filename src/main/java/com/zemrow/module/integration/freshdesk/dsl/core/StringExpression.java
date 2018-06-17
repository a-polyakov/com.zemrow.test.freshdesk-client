package com.zemrow.module.integration.freshdesk.dsl.core;

/**
 * Выражение String типа
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public abstract class StringExpression extends Expression<String>{
    /**
     * {@inheritDoc}
     */
    public StringExpression(String fieldName) {
        super(fieldName);
    }

    /**
     * {@inheritDoc}
     */
    @Override protected String toQuery(String object) {
        //TODO escape
        return '\''+object+'\'';
    }
}
