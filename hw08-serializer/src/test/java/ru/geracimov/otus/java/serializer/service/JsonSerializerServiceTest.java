package ru.geracimov.otus.java.serializer.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    public void serializeCustomClass3Test() {
        Object customClass = new CustomClass3();
        String serializedJson = jsonSerializer.serialize(customClass);
        String gsonJson = gson.toJson(customClass);

        assertThat(serializedJson).isEqualTo(gsonJson);
    }

    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    void customTest(Object o){
        assertThat(jsonSerializer.serialize(o)).isEqualTo(gson.toJson(o));
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte)1), Arguments.of((short)2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new byte[] {1, 2, 3}),
                Arguments.of(new short[] {4, 5, 6}),
                Arguments.of(new int[] {7, 8, 9}),
                Arguments.of(new float[] {10f, 11f, 12f}),
                Arguments.of(new double[] {13d, 14d, 15d}),
                Arguments.of(List.of(16, 17, 18)),
                Arguments.of(Collections.singletonList(19))
        );
    }
}