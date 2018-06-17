package com.zemrow.module.integration.freshdesk.dsl.core;

/**
 * Выражение
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public abstract class Expression<T> {

    /**
     * Наименование поля
     */
    protected final String fieldName;
    /**
     * Кеширование выражения проверти на null
     */
    private transient volatile BooleanExpression isnull;

    /**
     * Создание выражения по полю
     * @param fieldName наименование поля
     */
    public Expression(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Create a {@code this is null} expression
     *
     * @return this is null
     */
    public BooleanExpression isNull() {
        if (this.isnull == null) {
            this.isnull = new BooleanExpression(fieldName+":null");
        }

        return this.isnull;
    }

    /**
     * Create a {@code this == right} expression
     *
     * <p>Use expr.isNull() instead of expr.eq(null)</p>
     *
     * @param right rhs of the comparison
     * @return this == right
     */
    public BooleanExpression eq(T right) {
        if (right == null) {
            throw new IllegalArgumentException("eq(null) is not allowed. Use isNull() instead");
        } else {
            return new BooleanExpression(fieldName+":"+toQuery(right));
        }
    }

    /**
     * Конвертирование значения в строку для запроса
     * @param object значение
     * @return представление значения для запроса
     */
    protected String toQuery(T object){
        return String.valueOf(object);
    }

}
