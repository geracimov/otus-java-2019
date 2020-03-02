package ru.geracimov.otus.java.atmdepartament.bank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.atmdepartament.exception.AtmMoneyNotEnoughException;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static ru.geracimov.otus.java.atmdepartament.money.Denomination.*;

@DisplayName("Стратегия выдачи купюр минимальным количеством")
public class MinNotesGiveOutStrategyTest {

    private static Map<Denomination, Long> leftovers;
    private static GiveOutCalculationStrategy strategy;

    @BeforeAll
    static void beforeAll() {
        leftovers = new HashMap<>();
        leftovers.put(B500, 10L);
        leftovers.put(B100, 45L);
        leftovers.put(B1000, 3L);
        strategy = new MinNotesGiveOutStrategy();
    }

    @Test
    @DisplayName("корректно считает")
    public void calculateCorrectTest() {
        assertThat(strategy.calculate(leftovers, 1500L)).containsOnly(entry(B500, 1L), entry(B1000, 1L));
        assertThat(strategy.calculate(leftovers, 900L)).containsOnly(entry(B500, 1L), entry(B100, 4L));
        assertThat(strategy.calculate(leftovers, 3000L)).containsOnly(entry(B1000, 3L));
        assertThat(strategy.calculate(leftovers, 100L)).containsOnly(entry(B100, 1L));
        assertThat(strategy.calculate(leftovers, 12500L)).containsOnly(entry(B100, 45L), entry(B1000, 3L), entry(B500, 10L));
    }

    @Test
    @DisplayName("выдает ошибку при нехватке денег")
    public void calculateLackMoneyTest() {
        assertThatThrownBy(() -> strategy.calculate(leftovers, 12600L))
                .isInstanceOf(AtmMoneyNotEnoughException.class)
                .hasMessage("Cannot give out requested amount '12600', not enough '100'");
    }

    @Test
    @DisplayName("бросает исключение при невозможности выдать сумму имеющимся банкнотами")
    public void cannotCalculateTest() {
        assertThatThrownBy(() -> strategy.calculate(leftovers, 555L))
                .isInstanceOf(AtmMoneyNotEnoughException.class)
                .hasMessage("Cannot give out requested amount '555', not enough '55'");
    }


}