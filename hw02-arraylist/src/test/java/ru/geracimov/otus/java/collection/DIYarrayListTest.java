package ru.geracimov.otus.java.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("Реализация DIYarrayList")
class DIYarrayListTest {

    private DIYarrayList<Long> longList;

    @BeforeEach
    void init() {
        longList = new DIYarrayList<>();
        for (long l = 0L; l < 100L; l++) {
            longList.add(l);
        }
    }

    @Test
    @DisplayName("Корректно возвращает размер пустого списка")
    void emptyListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Бросает исключение при инициализации списка с отрицательным размером")
    void initListSizeNegativeSizeTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> new DIYarrayList<>(-1))
                .withMessageMatching("Size must be positive");
    }

    @Test
    @DisplayName("Корректно возвращает признак пустого списка")
    void emptyListIsEmptyTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
        //noinspection ConstantConditions
        assertThat(strings.isEmpty()).isEqualTo(true);
        strings.add("something");
        assertThat(strings.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("Корректно возвращает размер пустого списка с заданным начальным размером")
    void emptyInitListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>(10);
        assertThat(strings.size()).isEqualTo(0);
        strings.add("something1");
        strings.add("something2");
        assertThat(strings.size()).isEqualTo(2);
    }

    @Disabled
    @Test
    @DisplayName("Корректно добавляет элементы в коллекцию")
    void addElementInListTest() {
        longList.add(1, 0L);
        longList.add(1, -1L);
        assertThat(longList).containsSequence(0L, 1L, -1L, 0L, 2L, 3L);
        assertThat(longList).hasSize(5);
    }

    @Disabled
    @Test
    @DisplayName("Корректно добавляет несколько элементов в коллекцию")
    void addAllElementInListTest() {
        Collections.addAll(longList, 100L, 101L, 102L, 103L);
        assertThat(longList).containsSequence(98L, 99L, 100L, 101L, 102L, 103L);
        assertThat(longList).hasSize(104);
    }

    @Disabled
    @Test
    @DisplayName("Корректно удаляет элементы из коллекции")
    void dddd() {
        longList.remove(6L);
        longList.remove(7L);
        longList.remove(54L);
        assertThat(longList).hasSize(97);
        assertThat(longList).doesNotContain(6L, 7L, 54L);
    }

}