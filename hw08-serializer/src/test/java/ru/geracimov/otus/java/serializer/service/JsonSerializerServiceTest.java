package ru.geracimov.otus.java.serializer.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerializerServiceTest {
    private static JsonSerializerService jsonSerializer;
    public static Gson gson = new Gson();

    @BeforeAll
    public static void setUp() {
        jsonSerializer = new JsonSerializerService();
        gson = new Gson();
    }

    @Test
    public void serializeCustomClass1Test() {
        Object customClass = new CustomClass1();
        String serializedJson = jsonSerializer.serialize(customClass);
        String gsonJson = gson.toJson(customClass);

        assertThat(serializedJson).isEqualTo(gsonJson);
    }

    @Test
    public void serializeCustomClass2Test() {
        Object customClass = new CustomClass2();
        String serializedJson = jsonSerializer.serialize(customClass);
        String gsonJson = gson.toJson(customClass);

        assertThat(serializedJson).isEqualTo(gsonJson);
    }

}