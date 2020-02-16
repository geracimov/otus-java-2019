package ru.geracimov.otus.java.gc.langolier.helper;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LangolierHelperTest {

    private static LangolierHelper helper;

    @BeforeAll
    public static void beforeAll() {
        helper = new LangolierHelper();
    }

    @Test
    public void printPercentagePassedTest() {
        assertThat(helper.printPercentage(0, 10, 50))
                .isEqualTo("100% [##################################################]");
        assertThat(helper.printPercentage(3, 10, 20))
                .isEqualTo("70% [##############......]");
        assertThat(helper.printPercentage(8, 10, 30))
                .isEqualTo("20% [######........................]");
        assertThat(helper.printPercentage(10, 10, 10))
                .isEqualTo("0% [..........]");
    }

    @Test
    public void printPercentageErrorTest() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> helper.printPercentage(1, -10, 50));
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> helper.printPercentage(-1, 0, 50));
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> helper.printPercentage(1, 10, -50));
    }

}