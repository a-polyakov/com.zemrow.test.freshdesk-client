package com.zemrow.module.integration.freshdesk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import com.zemrow.module.integration.freshdesk.exception.ObjectNotFoundException;
import com.zemrow.module.integration.freshdesk.exception.RequestException;
import com.zemrow.module.integration.freshdesk.exception.TooManyRequestsException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Обертка для работы с freshdesk api
 *
 * @author Alexandr Polyakov on 2018.06.17
 * @see <a href="https://developers.freshdesk.com/api/">https://developers.freshdesk.com/api/</a>
 */
public class FreshdeskClient {

    private final String freshdeskUrl;
    private final String authorization;
    /**
     * Доступное количество запросов
     *
     * @see <a href="https://developers.freshdesk.com/api/#ratelimit">ratelimit</a>
     */
    private final AtomicInteger xRatelimitRemaining;

    /**
     * Создание клиента используя apiKey
     *
     * @param freshdeskUrl путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param apiKey       ключ для доступа к api
     */
    public FreshdeskClient(String freshdeskUrl, String apiKey) {
        this(freshdeskUrl, apiKey, "X");
    }

    /**
     * Создание клиента
     *
     * @param freshdeskUrl путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param username     логин
     * @param password     пароль
     */
    public FreshdeskClient(String freshdeskUrl, String username, String password) {
        this.freshdeskUrl = freshdeskUrl;
        authorization = "Basic " + Base64.getEncoder().encodeToString((username + ':' + password).getBytes(StandardCharsets.UTF_8));
        xRatelimitRemaining = new AtomicInteger(5000);
    }

    /**
     * Найти задачи
     *
     * @param predicates условия поиска (например TicketDsl.status.eq(TicketStatus.Open))
     * @return набор задач
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#filter_tickets">filter_tickets</a>
     */
    public List<JSONObject> filterTickets(BooleanExpression... predicates) throws IOException {
        BooleanExpression first = null;
        if (predicates != null) {
            for (BooleanExpression predicate : predicates) {
                if (first == null) {
                    first = predicate;
                }
                else {
                    first = first.and(predicate);
                }
            }
        }

        int page = 1;
        JSONObject jsonObject = getTicketsInPage(first, page);
        int total = jsonObject.getInt("total");
        final List<JSONObject> list = new ArrayList<>(total);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            final JSONObject ticket_json = results.getJSONObject(i);
            list.add(ticket_json);
            total--;
        }

        while (total > 0) {
            page++;
            if (page > 10) {
                System.out.println("TODO");
                break;
            }
            else {
                jsonObject = getTicketsInPage(first, page);
                results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    final JSONObject ticket_json = results.getJSONObject(i);
                    list.add(ticket_json);
                    total--;
                }
            }
        }
        return list;
    }

    private JSONObject getTicketsInPage(BooleanExpression first, int page) throws IOException {
        String fullUrl = freshdeskUrl + "/api/v2/search/tickets?page=" + page + "";
        if (first != null) {
            fullUrl += "&query=\"" + URLEncoder.encode(first.toQuery(), "UTF-8") + '"';
        }
        JSONObject jsonObject = getJsonResponse(new URL(fullUrl));
        return jsonObject;
    }

    /**
     * Найти задачу по идентификатору
     *
     * @param ticketId идентификатор задачи
     * @return задача
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#view_a_ticket">view_a_ticket</a>
     */
    public JSONObject getTicket(int ticketId) throws IOException {
        return getJsonResponse("/api/v2/tickets/" + ticketId);
    }

    /**
     * Получить комментарии из задачи
     *
     * @param ticketId идентификатор задачи
     * @return
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#list_all_ticket_notes">list_all_ticket_notes</a>
     */
    public JSONArray getTicketComment(int ticketId) throws IOException {
        return getJsonArrayResponse("/api/v2/tickets/" + ticketId + "/conversations");
    }

    /**
     * Информация о текущем пользователе
     *
     * @return json
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#me">me</a>
     */
    public JSONObject me() throws IOException {
        return getJsonResponse("/api/v2/agents/me");
    }

    /**
     * Информация о пользователе
     *
     * @param userId
     * @return json
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#view_contact">view_contact</a>
     */
    public JSONObject getContact(long userId) throws IOException {
        return getJsonResponse("/api/v2/contacts/" + userId);
    }

    /**
     * Информация о пользователе
     *
     * @param userId
     * @return json
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#view_agent">view_agent</a>
     */
    public JSONObject getAgent(Long userId) throws IOException {
        return getJsonResponse("/api/v2/agents/" + userId);
    }

    /**
     * Обновить задачу
     *
     * @param ticketId идентификатор задачи
     * @param update   обновляемые поля
     * @return задача
     * @throws IOException
     * @see <a href="https://developers.freshdesk.com/api/#update_ticket">update_ticket</a>
     */
    public JSONObject updateTicket(int ticketId, JSONObject update) throws IOException {
        URL fullUrl = new URL(freshdeskUrl + "/api/v2/tickets/" + ticketId);
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
     * Выполнить http запрос, получить строку
     *
     * @param url
     * @return текст ответа
     * @throws IOException
     */
    private String getString(URL url) throws IOException {
        final HttpURLConnection connection = getConnection(url);
        return getString(connection);
    }

    /**
     * Отправить http запрос, получить json объект
     *
     * @param path
     * @return json объект
     * @throws IOException
     */
    public JSONObject getJsonResponse(String path) throws IOException {
        return getJsonResponse(new URL(freshdeskUrl + path));
    }

    /**
     * Отправить http запрос, получить json объект
     *
     * @param url
     * @return json объект
     * @throws IOException
     */
    private JSONObject getJsonResponse(URL url) throws IOException {
        final HttpURLConnection connection = getConnection(url);
        return getJsonResponse(connection);
    }

    /**
     * Отправить http запрос, получить json массив
     *
     * @param path
     * @return json массив
     * @throws IOException
     */
    public JSONArray getJsonArrayResponse(String path) throws IOException {
        return getJsonArrayResponse(new URL(freshdeskUrl + path));
    }

    /**
     * Отправить http запрос, получить json массив
     *
     * @param url
     * @return json массив
     * @throws IOException
     */
    private JSONArray getJsonArrayResponse(URL url) throws IOException {
        final HttpURLConnection connection = getConnection(url);
        return getJsonArrayResponse(connection);
    }

    /**
     * Создать соединение, установить учетные данные
     *
     * @param url
     * @return соединение
     * @throws IOException
     */
    private HttpURLConnection getConnection(URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", authorization);
        return connection;
    }

    /**
     * Проверить соединение на ошибку
     *
     * @param connection соединение
     * @throws IOException
     */
    private void checkError(final HttpURLConnection connection) throws IOException {
        final int responseCode = connection.getResponseCode();
        if (responseCode == ObjectNotFoundException.RESPONSE_CODE) {
            throw new ObjectNotFoundException(connection);
        }
        if (responseCode == TooManyRequestsException.RESPONSE_CODE) {
            throw new TooManyRequestsException(connection);
        }
        else if (responseCode != 200) {
            throw new RequestException(connection);
        }
        final String xRatelimitRemainingStr = connection.getHeaderField("x-ratelimit-remaining");
        if (xRatelimitRemainingStr != null) {
            try {
                xRatelimitRemaining.set(Integer.parseInt(xRatelimitRemainingStr));
            }
            catch (Exception e) {
                //TODO
                e.printStackTrace();
            }
        }
    }

    /**
     * Получить поток на чтение из соединения
     *
     * @param connection соединение
     * @return строковый поток
     * @throws IOException
     */
    private InputStreamReader getReaderResponse(final HttpURLConnection connection) throws IOException {
        checkError(connection);
        return new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
    }

    /**
     * Прочитать из соединения json объект
     *
     * @param connection соединение
     * @return json объект
     * @throws IOException
     */
    private JSONObject getJsonResponse(final HttpURLConnection connection) throws IOException {
        try (final InputStreamReader reader = getReaderResponse(connection)) {
            final JSONObject result = new JSONObject(new JSONTokener(reader));
            return result;
        }
    }

    /**
     * Прочитать из соединения json массив
     *
     * @param connection соединение
     * @return json массив
     * @throws IOException
     */
    private JSONArray getJsonArrayResponse(final HttpURLConnection connection) throws IOException {
        try (final InputStreamReader reader = getReaderResponse(connection)) {
            final JSONArray result = new JSONArray(new JSONTokener(reader));
            return result;
        }
    }

    /**
     * Прочитать из соединения строку
     * @param connection
     * @return строка
     * @throws IOException
     */
    private String getString(final HttpURLConnection connection) throws IOException {
        checkError(connection);
        byte buffer[] = new byte[4048];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final InputStream is = connection.getInputStream()) {
            int l;
            while ((l = is.read(buffer)) > 0) {
                out.write(buffer, 0, l);
            }
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * История изменения задачи
     *
     * @param id идентификатор задачи
     * @return
     */
    //TODO
//    public List<JSONObject> getActivities(int id) throws IOException {
//        int page=1;
//        JSONObject jsonObject = getActivities(id, page);
//        int total=jsonObject.getInt("total");
//        final List<JSONObject> list = new ArrayList<>(total);
//        JSONArray results = jsonObject.getJSONArray("results");
//        for (int i = 0; i < results.length(); i++) {
//            final JSONObject ticket_json = results.getJSONObject(i);
//            list.add(ticket_json);
//            total--;
//        }
//
//        while (total>0){
//            page++;
//            jsonObject = getActivities(id, page);
//            results = jsonObject.getJSONArray("results");
//            for (int i = 0; i < results.length(); i++) {
//                final JSONObject ticket_json = results.getJSONObject(i);
//                list.add(ticket_json);
//                total--;
//            }
//        }
//        return list;
//
//    }
    //TODO
    public String getActivities(int id, int page) throws IOException {
        // String fullUrl = freshdeskUrl + "/helpdesk/tickets/"+id+"/activitiesv2/?limit=20
        // String fullUrl = freshdeskUrl + "/api/v2/export/ticket_activities
        // String fullUrl = freshdeskUrl + "/api/v2/tickets/"+id+"/?page="+page;
        String fullUrl = freshdeskUrl + "/helpdesk/tickets/" + id + "/activitiesv2?page=" + page;
//        JSONObject jsonObject = getJsonResponse(new URL(fullUrl));
        final String body = getString(new URL(fullUrl));
        return body;
    }

    /**
     * Получить вложение
     * @param id
     * @return
     * @throws IOException
     */
    public InputStream getAttachments(long id) throws IOException {
        //TODO
        // String fullUrl = freshdeskUrl + "/api/v2/attachments/"+id+"?download=true";
        String fullUrl = freshdeskUrl + "/helpdesk/attachments/" + id + "?download=true";
        final HttpURLConnection connection = getConnection(new URL(fullUrl));
        return connection.getInputStream();
    }

    /**
     * Информация о компании
     * @param id
     * @return json
     * @throws IOException
     */
    public JSONObject getCompany(long id) throws IOException {
        final JSONObject company = getJsonResponse("/api/v2/companies/" + id);
        return company;
    }

    /**
     * @return Доступное количество запросов
     */
    public int getXRatelimitRemaining() {
        return xRatelimitRemaining.get();
    }
}
