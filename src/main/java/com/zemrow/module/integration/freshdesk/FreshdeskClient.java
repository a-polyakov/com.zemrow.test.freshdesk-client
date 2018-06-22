package com.zemrow.module.integration.freshdesk;

import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Обертка для работы с freshdesk api
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class FreshdeskClient {

    private final String freshdeskUrl;
    private final String authorization;

    /**
     * Создание клиента используя apiKey
     *
     * @param freshdeskUrl путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param apiKey ключ для доступа к api
     */
    public FreshdeskClient(String freshdeskUrl, String apiKey) {
        this(freshdeskUrl, apiKey, "X");
    }

    /**
     * Создание клиента
     *
     * @param freshdeskUrl путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param username логин
     * @param password пароль
     */
    public FreshdeskClient(String freshdeskUrl, String username, String password) {
        this.freshdeskUrl = freshdeskUrl;
        authorization = "Basic " + Base64.getEncoder().encodeToString((username + ':' + password).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Найти задачи
     *
     * @param predicates условия поиска (например TicketDsl.status.eq(TicketStatus.Open))
     * @return json с набором задач ({"results": [массив задач], "total": количество_задач})
     * @throws IOException
     */
    public JSONObject filterTickets(BooleanExpression... predicates) throws IOException {
        BooleanExpression first = null;
        if (predicates != null) {
            for (BooleanExpression predicate : predicates) {
                if (first == null) {
                    first = predicate;
                } else {
                    first = first.and(predicate);
                }
            }
        }

        String fullUrl = freshdeskUrl + "/api/v2/search/tickets";
        if (first != null) {
            fullUrl += "?query=\"" + URLEncoder.encode(first.toQuery(), "UTF-8") + '"';
        }
        return get(new URL(fullUrl));
    }

    /**
     * Найти задачу по идентификатору
     * @param id идентификатор задачи
     * @return задача
     * @throws IOException
     */
    public JSONObject find(int id) throws IOException {
        return get(new URL(freshdeskUrl + "/api/v2/tickets/" + id));
    }

    /**
     * Обновить задачу
     * @param id идентификатор задачи
     * @param update обновляемые поля
     * @return задача
     * @throws IOException
     */
    public JSONObject update(int id, JSONObject update) throws IOException {
        URL fullUrl = new URL(freshdeskUrl + "/api/v2/tickets/" + id);
        final HttpURLConnection connection = (HttpURLConnection)fullUrl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        try (final OutputStream os = connection.getOutputStream()) {
            os.write(update.toString().getBytes(StandardCharsets.UTF_8));
        }
        return getJsonResponse(connection);
    }

    /**
     * Обправить get http запрос
     * @param url
     * @return json объект
     * @throws IOException
     */
    private JSONObject get(URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", authorization);
        return getJsonResponse(connection);
    }

    /**
     * Прочитать из соединения json объект
     * @param connection соединение
     * @return json объект
     * @throws IOException
     */
    private JSONObject getJsonResponse(final HttpURLConnection connection) throws IOException {
        final int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            //TODO
            throw new RuntimeException("request:" + connection.getURL().toString() + "\nresponseCode:" + responseCode + "\nresponse" + connection.getResponseMessage());
        }
        try(final InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
            final JSONObject result = new JSONObject(new JSONTokener(reader));
            return result;
        }
    }
}
