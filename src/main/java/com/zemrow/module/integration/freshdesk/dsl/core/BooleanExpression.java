package com.zemrow.module.integration.freshdesk.dsl.core;

/**
 * Выражение boolean типа
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class BooleanExpression extends Expression<Boolean> {
    /**
     * признак наличия в выражении оператора
     */
    protected final ExpressionOperator expressionOperator;

    /**
     * {@inheritDoc}
     */
    public BooleanExpression(String fieldName) {
        this(fieldName, null);
    }

    /**
     *
     * @param query выражение
     * @param expressionOperator признак наличия в выражении оператора (используется OR, AND)
     */
    public BooleanExpression(String query, ExpressionOperator expressionOperator) {
        super(query);
        this.expressionOperator = expressionOperator;
    }

    /**
     * Create a {@code this && right} expression
     *
     * <p>Returns an intersection of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return {@code this &amp;&amp; right}
     */
    public BooleanExpression and(BooleanExpression right) {
        if (right != null) {
            String leftQuery = fieldName;
            if (expressionOperator == ExpressionOperator.OR) {
                leftQuery = '(' + leftQuery + ')';
            }
            String rightQuery = right.toQuery();
            if (right.expressionOperator == ExpressionOperator.OR) {
                rightQuery = '(' + rightQuery + ')';
            }
            return new BooleanExpression(leftQuery + " and " + rightQuery, ExpressionOperator.AND);
        }
        else {
            return this;
        }
    }

    /**
     * Create a {@code this || right} expression
     *
     * <p>Returns a union of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return {@code this || right}
     */
    public BooleanExpression or(BooleanExpression right) {
        if (right != null) {
            String leftQuery = fieldName;
            if (expressionOperator == ExpressionOperator.OR) {
                leftQuery = '(' + leftQuery + ')';
            }
            String rightQuery = right.toQuery();
            if (right.expressionOperator == ExpressionOperator.AND) {
                rightQuery = '(' + rightQuery + ')';
            }
            return new BooleanExpression(leftQuery + " OR " + rightQuery, ExpressionOperator.OR);
        }
        else {
            return this;
        }
    }

    /**
     * Конвертирование в строку для запроса
     *
     * @return строка с запросом
     */
    public String toQuery() {
        return fieldName;
    }
}
