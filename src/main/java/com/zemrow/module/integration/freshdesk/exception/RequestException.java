package com.zemrow.module.integration.freshdesk.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Ошибка обработки запроса
 *
 * @author Alexandr Polyakov on 2019.05.27
 */
public class RequestException extends RuntimeException {
    private final String url;
    private final int responseCode;
    private final String responseMessage;
    private final StringBuilder body;

    public RequestException(HttpURLConnection connection) throws IOException {
        this.url = connection.getURL().toString();
        this.responseCode = connection.getResponseCode();
        this.responseMessage = connection.getResponseMessage();
        this.body = new StringBuilder();
        final InputStream stream = connection.getErrorStream();
        if (stream != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    this.body.append(line);
                }
            }
        }
    }

    @Override public String getMessage() {
        return "request:" + url + "\nresponseCode:" + responseCode + "\nresponse:" + responseMessage + "\nbody:" + body;
    }

    public String getUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public StringBuilder getBody() {
        return body;
    }
}
