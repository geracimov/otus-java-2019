package ru.geracimov.otus.java.atmdepartament;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.geracimov.otus.java.atmdepartament.bank.GiveOutCalculationStrategy;
import ru.geracimov.otus.java.atmdepartament.bank.MinNotesGiveOutStrategy;
import ru.geracimov.otus.java.atmdepartament.bank.RandomConfigServer;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.CashBundle;
import ru.geracimov.otus.java.atmdepartament.money.CashBundleItem;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.simple.SimpleAtm;
import ru.geracimov.otus.java.atmdepartament.simple.SimpleAtmConfiguration;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmCassette;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmKeyboard;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ru.geracimov.otus.java.atmdepartament.money.Denomination.B100;
import static ru.geracimov.otus.java.atmdepartament.money.Denomination.B500;

public class AtmDepartmentTest {
    public static final SimpleAtm ATM1 = new SimpleAtm(1);
    public static final SimpleAtm ATM2 = new SimpleAtm(2);
    public static final SimpleAtm ATM3 = new SimpleAtm(3);
    @Spy
    private RandomConfigServer configServer;
    private AtmDepartment department;
    private static List<AtmBackend> atmList;

    private static AtmConfiguration typicalConfig;

    @BeforeAll
    public static void beforeAll() {
        atmList = List.of(ATM1, ATM2, ATM3);

        GiveOutCalculationStrategy strategy = new MinNotesGiveOutStrategy();
        List<AtmCassette> cassettes = List.of(
                new SimpleAtmCassette(Currency.RUR, B500, 100),
                new SimpleAtmCassette(Currency.USD, B100, 50));

        SimpleAtmCashDispenser dispenser = new SimpleAtmCashDispenser(cassettes, strategy);
        typicalConfig = SimpleAtmConfiguration.builder().cashDispenser(dispenser)
                .atmKeyboard(new SimpleAtmKeyboard())
                .atmDisplay(new SimpleDisplay()).build();

    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(configServer.loadConfiguration(ATM1)).thenReturn(typicalConfig.clone());
        when(configServer.loadConfiguration(ATM2)).thenReturn(typicalConfig.clone());
        when(configServer.loadConfiguration(ATM3)).thenReturn(typicalConfig.clone());

        department = new AtmDepartmentImpl(configServer);
        department.addAtm(atmList);
        department.loadAtmConfigurations();
    }

    @Test
    public void getAtmCount() {
        assertThat(department.getAtmCount()).isEqualTo(3);
    }

    @Test
    public void integrationTest() {
        Map<Currency, Long> balance = department.getBalance();
        assertThat(balance).containsOnlyKeys(Currency.RUR, Currency.USD);
        assertThat(balance).extractingByKey(Currency.RUR).isEqualTo(150000L);
        assertThat(balance).extractingByKey(Currency.USD).isEqualTo(15000L);

        ATM1.userInterface().giveOut(Currency.RUR, 16000);
        ATM2.userInterface().giveOut(Currency.USD, 1000);

        balance = department.getBalance();
        assertThat(balance).extractingByKey(Currency.RUR).isEqualTo(134000L);
        assertThat(balance).extractingByKey(Currency.USD).isEqualTo(14000L);

        final HashMap<Currency, CashBundle> cash = new HashMap<>();
        CashBundle bundle = new CashBundle();
        bundle.put(new CashBundleItem(B500, 10L));
        cash.put(Currency.RUR, bundle);

        CashBundle bundle2 = new CashBundle();
        bundle2.put(new CashBundleItem(B500, 10L));
        final HashMap<Currency, CashBundle> cash2 = new HashMap<>();
        cash2.put(Currency.USD, bundle2);


        ATM3.userInterface().accept(cash);
        assertThat(ATM2.userInterface().accept(cash2)).isEqualTo(cash2);

        balance = department.getBalance();

        assertThat(balance).extractingByKey(Currency.RUR).isEqualTo(139000L);
        assertThat(balance).extractingByKey(Currency.USD).isEqualTo(14000L);
    }

    @Test
    public void addAtm() {
        department.addAtm(new SimpleAtm(5));
        assertThat(department.getAtmCount()).isEqualTo(4);
    }


}