package ru.geracimov.otus.java.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("Реализация DIYarrayList")
class DIYarrayListTest {


    @Test
    @DisplayName("Корректно возвращает размер пустого списка")
    void emptyListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
        assertThat(strings.size()).isEqualTo(0);
        assertThat(strings.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("Бросает исключение при инициализации списка с отрицательным размером")
    void initListSizeNegativeSizeTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            DIYarrayList<String> strings = new DIYarrayList<>(-1);
        }).withMessageMatching("Size must be positive");
    }

    @Test
    @DisplayName("Корректно возвращает признак пустого списка")
    void emptyListIsEmptyTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
    }

    @Test
    @DisplayName("Корректно возвращает размер пустого списка с заданным начальным размером")
    void emptyInitListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>(10);
        assertThat(strings.size()).isEqualTo(0);
    }

    @Test
    void name() {
        int[] src = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] dst = new int[src.length * 2];
        System.out.println(Arrays.toString(Arrays.copyOf(src, 4)));
        System.arraycopy(src, 0, dst, 0, src.length);
        dst[src.length] = 99;
        System.out.println(Arrays.toString(dst));
    }
}