package ru.geracimov.otus.java.atm.simple;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.atm.AtmCashDispenser;
import ru.geracimov.otus.java.atm.AtmCassette;
import ru.geracimov.otus.java.atm.AtmConfiguration;
import ru.geracimov.otus.java.atm.exception.AtmException;
import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;
import ru.geracimov.otus.java.atm.simple.devices.SimpleAtmCashDispenser;
import ru.geracimov.otus.java.atm.simple.devices.SimpleAtmCassette;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Модель банкомата simple")
public class SimpleAtmTest {
    private static SimpleAtm atm;

    @BeforeAll
    static void beforeAll() {
        atm = new SimpleAtm(3333);
    }

    @Test
    @DisplayName("имеет 4 отсека для кассет")
    public void cassettesCount() {
        assertThat(atm.getAtmId()).isEqualTo(3333);
        assertThat(atm.cassettesLimit()).isEqualTo(4);
    }

    @Test
    @DisplayName("бросает исключение при попытке инициализировать банкомат числом кассет, превышающим лимит")
    public void apply() {
        List<AtmCassette> cassettes = new ArrayList<>();
        cassettes.add(new SimpleAtmCassette(Currency.RUR, Denomination.B1000, 100));
        cassettes.add(new SimpleAtmCassette(Currency.RUR, Denomination.B1000, 100));
        cassettes.add(new SimpleAtmCassette(Currency.RUR, Denomination.B1000, 100));
        cassettes.add(new SimpleAtmCassette(Currency.RUR, Denomination.B1000, 100));
        cassettes.add(new SimpleAtmCassette(Currency.RUR, Denomination.B1000, 100));
        AtmCashDispenser dispenser = new SimpleAtmCashDispenser(cassettes, null);
        AtmConfiguration configuration = new SimpleAtmConfiguration(dispenser, null, null);

        assertThatThrownBy(() -> atm.apply(configuration)).isInstanceOf(AtmException.class).hasMessage("AtmSimple accept 4 cassettes max. Your: 5");
    }
}