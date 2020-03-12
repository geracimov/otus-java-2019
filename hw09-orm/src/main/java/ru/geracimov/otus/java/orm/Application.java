package ru.geracimov.otus.java.orm;

import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.orm.example.User;
import ru.geracimov.otus.java.serializer.service.InsertSerializerService;
import ru.geracimov.otus.java.serializer.service.SelectSerializerService;
import ru.geracimov.otus.java.serializer.service.VisitorService;

@Slf4j
public class Application {

    public static void main(String[] args) {

        VisitorService service = new SelectSerializerService();
        final InsertSerializerService service2 = new InsertSerializerService();
        final User user = new User(1, "username", (short) 36);
        System.out.println("service.serialize(user) = " + service.serialize(user));
        System.out.println("service.serialize(user) = " + service2.serialize(user));
        final User user2 = new User(2, "username2", (short) 38);
        System.out.println("service.serialize(user) = " + service.serialize(user2));
        System.out.println("service.serialize(user) = " + service2.serialize(user2));
        final User user3 = new User(3, "username2", (short) 38);
        System.out.println("service.serialize(user) = " + service.serialize(user3));
        System.out.println("service.serialize(user) = " + service2.serialize(user3));
        final User user4 = new User(4, "username2", (short) 38);
        System.out.println("service.serialize(user) = " + service.serialize(user4));
        System.out.println("service.serialize(user) = " + service2.serialize(user4));
        final User user5 = new User(5, "username2", (short) 38);
        System.out.println("service.serialize(user) = " + service.serialize(user5));
        System.out.println("service.serialize(user) = " + service2.serialize(user5));
    }

}
