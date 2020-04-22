package ru.geracimov.otus.java.web.server.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
