package ru.geracimov.otus.java.web.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;
import ru.geracimov.otus.java.web.server.repository.RedisUserDao;
import ru.geracimov.otus.java.web.server.repository.UserDao;
import ru.geracimov.otus.java.web.server.model.User;
import ru.geracimov.otus.java.web.server.server.UsersWebServer;
import ru.geracimov.otus.java.web.server.server.UsersWebServerWithFilterBasedSecurity;
import ru.geracimov.otus.java.web.server.services.TemplateProcessor;
import ru.geracimov.otus.java.web.server.services.TemplateProcessorImpl;
import ru.geracimov.otus.java.web.server.services.UserAuthService;
import ru.geracimov.otus.java.web.server.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final int REDIS_SERVER_PORT = 6379;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        final RedisServer redisServer = new RedisServer(REDIS_SERVER_PORT);
        redisServer.start();
        JedisPool pool = new JedisPool();
        ObjectMapper objectMapper = new ObjectMapper();
        UserDao userDao = new RedisUserDao(pool, objectMapper);
        userDao.saveUser(new User(0, "admin", "admin", "adminPassword"));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                                                                                  authService,
                                                                                  userDao,
                                                                                  gson,
                                                                                  templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
        redisServer.stop();
    }
}
