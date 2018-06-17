package com.zemrow.module.integration.freshdesk;

import com.zemrow.module.integration.freshdesk.dsl.core.BooleanExpression;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.AuthSchemeBase;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Обертка для работы с freshdesk api
 *
 * TODO оптимизировать создание и использовать http client (содрано с официального примера https://github.com/freshdesk/fresh-samples/blob/master/JAVA/httpclient4x/src/main/java/com/freshdesk/httpclient4x/FilterTickets.java)
 *
 * @author Alexandr Polyakov on 2018.06.17
 */
public class FreshdeskClient {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Создание клиента используя apiKey
     *
     * @param url путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param apiKey ключ для доступа к api
     */
    public FreshdeskClient(String url, String apiKey) {
        this(url, apiKey, "X");
    }

    /**
     * Создание клиента
     *
     * @param url путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param username логин
     * @param password пароль
     */
    public FreshdeskClient(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Найти задачи
     *
     * @param predicates условия поиска (например TicketDsl.status.eq(TicketStatus.Open))
     * @return json с набором задач ({"results": [массив задач], "total": количество_задач})
     * @throws IOException
     * @throws URISyntaxException
     */
    public JSONObject filterTickets(BooleanExpression... predicates) throws IOException, URISyntaxException {
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

        return filterTickets0((first != null) ? first.toQuery() : "");
    }

    private JSONObject filterTickets0(String filterQuery) throws IOException, URISyntaxException {
        //TODO
        final HttpClientBuilder hcBuilder = HttpClientBuilder.create();
        final RequestBuilder reqBuilder = RequestBuilder.get();
        final RequestConfig.Builder rcBuilder = RequestConfig.custom();

        URL fullUrl = new URL(url + "/api/v2/search/tickets");
        final String urlHost = fullUrl.getHost();
        final int urlPort = fullUrl.getPort();
        final String urlProtocol = fullUrl.getProtocol();
        reqBuilder.setUri(fullUrl.toURI());
        reqBuilder.addParameter("query", '"' + filterQuery + '"');

        // Authentication:
        List<String> authPrefs = new ArrayList<>();
        authPrefs.add(AuthSchemes.BASIC);
        rcBuilder.setTargetPreferredAuthSchemes(authPrefs);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
            new AuthScope(urlHost, urlPort, AuthScope.ANY_REALM),
            new UsernamePasswordCredentials(username, password));
        hcBuilder.setDefaultCredentialsProvider(credsProvider);
        AuthCache authCache = new BasicAuthCache();
        AuthSchemeBase authScheme = new BasicScheme();
        authCache.put(new HttpHost(urlHost, urlPort, urlProtocol), authScheme);
        HttpClientContext hccContext = HttpClientContext.create();
        hccContext.setAuthCache(authCache);

        HttpEntity entity = new StringEntity("", ContentType.APPLICATION_JSON.withCharset(Charset.forName("utf-8")));
        reqBuilder.setEntity(entity);

        // Execute:
        RequestConfig rc = rcBuilder.build();
        reqBuilder.setConfig(rc);

        HttpClient hc = hcBuilder.build();
        HttpUriRequest req = reqBuilder.build();
        HttpResponse response = hc.execute(req, hccContext);

        HttpEntity body = response.getEntity();
        int response_status = response.getStatusLine().getStatusCode();

        if (response_status != 200) {
            //TODO
            throw new RuntimeException("request:" + req + "\nresponse:" + response);
        }

        JSONObject response_json = new JSONObject(new JSONTokener(new InputStreamReader(body.getContent(), StandardCharsets.UTF_8)));
        return response_json;
    }
}
