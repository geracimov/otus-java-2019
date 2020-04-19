package ru.geracimov.otus.java.web.server.server;

public interface UsersWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
