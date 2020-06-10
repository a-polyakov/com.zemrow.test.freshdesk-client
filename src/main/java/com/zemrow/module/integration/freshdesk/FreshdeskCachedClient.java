package com.zemrow.module.integration.freshdesk;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Обертка для работы с freshdesk api (с кешированием json на диск)
 *
 * @author Alexandr Polyakov on 2019.05.27
 */
public class FreshdeskCachedClient extends FreshdeskClient {
    public static final String COMPANIES_CACHE_DIR = "companies";
    public static final String CONTACTS_CACHE_DIR = "contacts";
    public static final String TICKET_CACHE_DIR = "tickets";
    public static final String TICKET_COMMENTS_CACHE_DIR = "ticket_comments";
    private final File cacheDir;
    private final File cacheDirCompanies;
    private final File cacheDirContacts;
    private final File cacheDirTickets;
    private final File cacheDirTicketComments;

    /**
     * Создание клиента
     *
     * @param freshdeskUrl путь к freshdesk (обычно имя_компании.freshdesk.com)
     * @param username логин
     * @param password пароль
     * @param cacheDir директория для кеширования запросов
     */
    public FreshdeskCachedClient(String freshdeskUrl, String username, String password, String cacheDir) {
        super(freshdeskUrl, username, password);
        this.cacheDir = new File(cacheDir);
        if (!this.cacheDir.isDirectory()) {
            this.cacheDir.mkdirs();
        }
        cacheDirCompanies = new File(cacheDir, COMPANIES_CACHE_DIR);
        if (!cacheDirCompanies.isDirectory()) {
            cacheDirCompanies.mkdir();
        }
        cacheDirContacts = new File(cacheDir, CONTACTS_CACHE_DIR);
        if (!cacheDirContacts.isDirectory()) {
            cacheDirContacts.mkdir();
        }
        cacheDirTickets = new File(cacheDir, TICKET_CACHE_DIR);
        if (!cacheDirTickets.isDirectory()) {
            cacheDirTickets.mkdir();
        }
        cacheDirTicketComments = new File(cacheDir, TICKET_COMMENTS_CACHE_DIR);
        if (!cacheDirTicketComments.isDirectory()) {
            cacheDirTicketComments.mkdir();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getCompany(long id) throws IOException {
        final JSONObject company;
        final File cache = new File(cacheDirCompanies, id + ".json");
        if (cache.isFile()) {
            try (final FileReader reader = new FileReader(cache)) {
                company = new JSONObject(new JSONTokener(reader));
            }
        }
        else {
            company = super.getCompany(id);
            try (final FileWriter writer = new FileWriter(cache)) {
                company.write(writer);
            }
        }
        return company;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getContact(long userId) throws IOException {
        final JSONObject contact;
        final File cache = new File(cacheDirContacts, userId + ".json");
        if (cache.isFile()) {
            try (final FileReader reader = new FileReader(cache)) {
                contact = new JSONObject(new JSONTokener(reader));
            }
        }
        else {
            contact = super.getContact(userId);
            try (final FileWriter writer = new FileWriter(cache)) {
                contact.write(writer);
            }
        }
        return contact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getTicket(int ticketId) throws IOException {
        final JSONObject ticket;
        final File cache = new File(cacheDirTickets, ticketId + ".json");
        if (cache.isFile()) {
            try (final FileReader reader = new FileReader(cache)) {
                ticket = new JSONObject(new JSONTokener(reader));
            }
        }
        else {
            ticket = super.getTicket(ticketId);
            try (final FileWriter writer = new FileWriter(cache)) {
                ticket.write(writer);
            }
        }
        return ticket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray getTicketComment(int ticketId) throws IOException {
        final JSONArray result;
        final File cache = new File(cacheDirTicketComments, ticketId + ".json");
        if (cache.isFile()) {
            try (final FileReader reader = new FileReader(cache)) {
                result = new JSONArray(new JSONTokener(reader));
            }
        }
        else {
            result = super.getTicketComment(ticketId);
            try (final FileWriter writer = new FileWriter(cache)) {
                result.write(writer);
            }
        }
        return result;
    }
}
