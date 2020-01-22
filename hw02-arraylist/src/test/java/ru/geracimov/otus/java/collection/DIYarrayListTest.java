package ru.geracimov.otus.java.collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Реализация DIYarrayList")
class DIYarrayListTest {

    private DIYarrayList<Long> longList;
    private DIYarrayList<Long> longListReverse;

    @BeforeEach
    public void init() {
        longList = new DIYarrayList<>();
        longListReverse = new DIYarrayList<>(50, 3.0f);
        for (long l = 0L; l < 100L; l++) {
            longList.add(l);
            longListReverse.add(0, l);
        }
    }

    @Test
    @DisplayName("Корректно возвращает размер пустого списка")
    public void emptyListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Бросает исключение при инициализации списка с отрицательным размером")
    public void initListSizeNegativeSizeTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> new DIYarrayList<>(-1))
                .withMessageMatching("Size must be positive");
    }

    @Test
    @DisplayName("Корректно возвращает признак пустого списка")
    public void emptyListIsEmptyTest() {
        DIYarrayList<String> strings = new DIYarrayList<>();
        //noinspection ConstantConditions
        assertThat(strings.isEmpty()).isEqualTo(true);
        strings.add("something");
        assertThat(strings.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("Корректно возвращает размер пустого списка с заданным начальным размером")
    public void emptyInitListSizeTest() {
        DIYarrayList<String> strings = new DIYarrayList<>(10);
        assertThat(strings.size()).isEqualTo(0);
        strings.add("something1");
        strings.add("something2");
        assertThat(strings.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Корректно возвращает результат проверки на вхождение элемента в коллекцию")
    public void containsElementTest() {
        DIYarrayList<String> strings = new DIYarrayList<>(10);
        strings.add("something1");
        strings.add("something2");
        assertThat(strings.contains("something1")).isTrue();
        assertThat(strings.contains("something3")).isFalse();
    }

    @Test
    @DisplayName("Корректно добавляет элементы в коллекцию")
    public void addElementInListTest() {
        longList.add(1, 0L);
        longList.add(1, -1L);
        assertThat(longList).containsSequence(0L, -1L, 0L, 1L, 2L, 3L);
        assertThat(longList).hasSize(102);
    }

    @Test
    @DisplayName("Бросает исключение при добавлении элемента с неверным индексом")
    public void addElementInListExceptionTest() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> longList.add(-5, 0L))
                .withMessage("Index: %d, Size: %d", -5, 100);
        assertThatExceptionOfType(IndexOutOfBoundsException.class)
                .isThrownBy(() -> longList.add(101, 0L))
                .withMessage("Index: %d, Size: %d", 101, 100);
    }

    @Test
    @DisplayName("Бросает исключение при получении индекса элемента null")
    public void indexOfIllegalArgumentTest() {
        DIYarrayList<String> strings = new DIYarrayList<>(10);
        strings.add("something1");
        strings.add("something2");
        assertThatIllegalArgumentException().isThrownBy(() -> strings.indexOf(null));
    }

    @Test
    @DisplayName("Корректно возвращает индекс последнего найденного элемента")
    public void lastIndexOfTest() {
        longList.add(99L);
        longList.add(99L);
        longList.add(99L);
        assertThat(longList.lastIndexOf(99L)).isEqualTo(102);
        assertThat(longList.lastIndexOf(55L)).isEqualTo(55);
        assertThat(longList.lastIndexOf(33L)).isEqualTo(33);
    }

    @Test
    @DisplayName("Корректно возвращает индекс несуществующего элемента")
    public void lastIndexOfUnexpectedTest() {
        assertThat(longList.lastIndexOf(200L)).isEqualTo(-1);
        assertThat(longList.lastIndexOf(-1L)).isEqualTo(-1);
    }

    @Test
    @DisplayName("Бросает исключение при определении индекса элемента null")
    public void lastIndexOfNullTest() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> longList.lastIndexOf(null));
    }

    @Test
    @DisplayName("Корректноо чищает внутреннее хранилище элементов")
    public void clearTest() {
        longList.clear();
        assertThat(longList).hasSize(0);
        assertThat(longList.isEmpty()).isTrue();
        assertThat(longList.size()).isZero();
    }

    @Test
    @DisplayName("Корректно добавляет несколько элементов в коллекцию через класс Collections")
    public void addAllElementInListTest() {
        Collections.addAll(longList, 100L, 101L, 102L, 103L);
        assertThat(longList).containsSequence(98L, 99L, 100L, 101L, 102L, 103L);
        assertThat(longList).hasSize(104);
    }

    @Test
    @DisplayName("Корректно добавляет несколько элементов в коллекцию")
    public void addAllElementsTest() {
        Collection<Long> collection = Arrays.asList(200L, 300L, 400L);
        longList.addAll(collection);
        assertThat(longList).hasSize(103);
        assertThat(longList).contains(200L, 300L, 400L);
    }

    @Test
    @DisplayName("Корректно удаляет элементы из коллекции")
    public void removeElementsTest() {
        longList.remove(6L);
        longList.remove(7L);
        longList.remove(54L);
        assertThat(longList).hasSize(97);
        assertThat(longList).doesNotContain(6L, 7L, 54L);
    }

    @Test
    @DisplayName("Бросает исключение при вызове addAll(index, {})")
    public void addAllOnIndexUnsupported() {
        Collection<Long> collection = Arrays.asList(200L, 300L, 400L);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> longList.addAll(1, collection));
    }

    @Test
    @DisplayName("Корректно копируется в другую коллекцию через Collections.copy")
    public void collectionsCopyTest() {
        assertThat(longListReverse.get(0)).isEqualTo(99L);
        assertThat(longListReverse.get(99)).isEqualTo(0L);

        Collections.copy(longListReverse, longList);
        assertThat(longListReverse).hasSize(100);
        assertThat(longListReverse).containsSequence(longList);
    }

    @Test
    @DisplayName("Корректно сортируется через Collections.sort")
    public void collectionsSortTest() {
        assertThat(longListReverse.get(0)).isEqualTo(99L);
        assertThat(longListReverse.get(99)).isEqualTo(0L);

        Collections.sort(longListReverse);
        assertThat(longListReverse).hasSize(100);
        assertThat(longListReverse.get(0)).isEqualTo(0L);
        assertThat(longListReverse.get(99)).isEqualTo(99L);
        assertThat(longListReverse).containsSequence(longList);
    }

}