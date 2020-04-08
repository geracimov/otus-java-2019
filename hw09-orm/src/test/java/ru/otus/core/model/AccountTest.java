package ru.otus.core.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Что касается setAccessible - предлагаю сделать тест. Получить от класса поле,
 * сделать ему setAccessible = true, а потом получить его еще раз и посмотреть значение isAccessible
 */
@DisplayName("setAccessible у поля класса можно не выставлять обратно в false")
public class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1, "someString", new BigDecimal(1));
    }

    @Test
    @SneakyThrows
    @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
    @DisplayName("поскольку при повторном обращении к полю класса создастся новый экземпляр с выключенным доступом")
    void setAccessibleTest() {
        final Field typeField = account.getClass().getDeclaredField("type");
        assertThat(typeField.isAccessible()).isFalse();
        assertThat(typeField.canAccess(account)).isFalse();

        typeField.setAccessible(true);
        assertThat(typeField.isAccessible()).isTrue();
        assertThat(typeField.canAccess(account)).isTrue();
        assertThat(typeField.get(account)).isEqualTo("someString");

        final Field typeFieldYet = account.getClass().getDeclaredField("type");
        assertThatThrownBy(() -> typeFieldYet.get(account)).isInstanceOf(IllegalAccessException.class);
        assertThat(typeFieldYet.isAccessible()).isFalse();
        assertThat(typeFieldYet.canAccess(account)).isFalse();
    }

}