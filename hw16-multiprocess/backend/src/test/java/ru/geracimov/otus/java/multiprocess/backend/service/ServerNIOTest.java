package ru.geracimov.otus.java.multiprocess.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = {"server.port=9001"})
@ExtendWith(SpringExtension.class)
public class ServerNIOTest {

    @Autowired
    private   ServerNIO serverNIO;


    @Test
    void integrationTest() throws Exception {
        serverNIO.destroy();
    }

}