package com.zemrow.module.integration.freshdesk.dsl.core;

/**
 * Выражение Number типа
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public abstract class NumberExpression extends Expression<Number> {

    /**
     * {@inheritDoc}
     */
    public NumberExpression(String fieldName) {
        super(fieldName);
    }
}
