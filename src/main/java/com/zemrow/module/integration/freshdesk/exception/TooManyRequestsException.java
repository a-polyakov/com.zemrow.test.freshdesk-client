package com.zemrow.module.integration.freshdesk.exception;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Ошибка превышения лимита запросов в час
 *
 * @author Alexandr Polyakov on 2019.05.27
 * @see <a href="https://developers.freshdesk.com/api/#ratelimit">API</a>
 */
public class TooManyRequestsException extends RequestException {
    /**
     * Код ошибки
     */
    public static final int RESPONSE_CODE = 429;
    /**
     * Количество секунд блокировки
     */
    private final int retryAfter;

    public TooManyRequestsException(HttpURLConnection connection) throws IOException {
        super(connection);
        this.retryAfter = Integer.parseInt(connection.getHeaderField("retry-after"));
    }

    public int getRetryAfter() {
        return retryAfter;
    }

    @Override public String toString() {
        return super.toString() + "\nretryAfter:" + retryAfter;
    }
}
