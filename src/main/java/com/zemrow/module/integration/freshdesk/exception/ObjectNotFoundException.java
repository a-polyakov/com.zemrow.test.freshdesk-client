package com.zemrow.module.integration.freshdesk.exception;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Ошибка объект не найден
 *
 * @author Alexandr Polyakov on 2019.05.27
 */
public class ObjectNotFoundException extends RequestException {

    public ObjectNotFoundException(HttpURLConnection connection) throws IOException {
        super(connection);
    }
}
