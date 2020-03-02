package ru.geracimov.otus.java.atmdepartament;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.exception.AtmException;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmCassette;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.geracimov.otus.java.atmdepartament.money.Currency.RUR;
import static ru.geracimov.otus.java.atmdepartament.money.Currency.USD;
import static ru.geracimov.otus.java.atmdepartament.money.Denomination.*;

@DisplayName("Кассета должна")
public class CassetteImplTest {

    @Test
    @DisplayName("содержать корректную корнфигурацию загруженных банкнот")
    public void cassetteConfigurationImplTest() {
        AtmCassette cassette = new SimpleAtmCassette(RUR, B100, 150);
        assertThat(cassette.balance()).isEqualTo(150);
        assertThat(cassette.loadedCurrency()).isEqualTo(RUR);
        assertThat(cassette.loadedDenomination()).isEqualTo(B100);
    }

    @Test
    @DisplayName("бросать исключение при попытке создания с конфигурацией несуществующих банкнот")
    public void cassetteConfigurationImplExceptionTest() {
        assertThatThrownBy(() -> new SimpleAtmCassette(USD, B1000, 0))
                .isInstanceOf(AtmException.class)
                .hasMessageContaining("US dollar banknote 1000 does not exist");
    }

    @Test
    @DisplayName("бросать исключение при попытке создания кассеты с отрицательным числом банкнот")
    public void cassetteConfigurationImplExceptionCapacityTest() {
        assertThatThrownBy(() -> new SimpleAtmCassette(USD, B1, -1))
                .isInstanceOf(AtmException.class)
                .hasMessageContaining("Balance cannot be less zero: -1");
    }

    @Test
    @DisplayName("уметь корректно уменьшать количество банкнот в ней")
    public void cassetteReduceCapacityTest() {
        AtmCassette cassette = new SimpleAtmCassette(RUR, B5000, 100);
        final Long capacityReturned = cassette.withdraw(17);
        final Long capacityRequested = cassette.balance();
        assertThat(capacityReturned).isEqualTo(83);
        assertThat(capacityRequested).isEqualTo(83);

        assertThatThrownBy(() -> cassette.withdraw(84))
                .isInstanceOf(AtmException.class)
                .hasMessageContaining("Requested count 84 more than balance 83");
    }

}