package ru.geracimov.otus.java.atm.simple;

import lombok.Value;
import ru.geracimov.otus.java.atm.AtmCashDispenser;
import ru.geracimov.otus.java.atm.AtmDisplay;
import ru.geracimov.otus.java.atm.AtmFacade;
import ru.geracimov.otus.java.atm.AtmKeyboard;
import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;

import java.util.Map;

@Value
public class SimpleAtmFacade implements AtmFacade {
    private final AtmDisplay display;
    private final AtmKeyboard keyboard;
    private final AtmCashDispenser dispenser;

    private SimpleAtmFacade(AtmDisplay display, AtmKeyboard keyboard, AtmCashDispenser dispenser) {
        this.display = display;
        this.keyboard = keyboard;
        this.dispenser = dispenser;
    }

    public static SimpleAtmFacade of(AtmDisplay display, AtmKeyboard kb, AtmCashDispenser dispenser) {
        return new SimpleAtmFacade(display, kb, dispenser);
    }

    @Override
    public Map<Denomination, Long> giveOut() {
        display.printText("interaction cash withdrawal");
        display.printText("choose currency");
        Currency currency = keyboard.chooseCurrency();
        display.printText("enter amount");
        final long requestedAmount = keyboard.chooseAmount();
        return dispenser.giveOut(currency, requestedAmount);
    }

    @Override
    public Map<Denomination, Long> giveOut(Currency currency, long amount) {
        display.printText("------------------------------------------------------------");
        display.printText("WITHDRAWAL " + currency.getName() + ": " + amount);
        display.printText("before withdrawal: " + dispenser.balance(currency) +
                ", avail: " + dispenser.balanceAvailable(currency, amount));
        Map<Denomination, Long> denominationLongMap = null;
        try {
            denominationLongMap = dispenser.giveOut(currency, amount);
            display.printText("WITHDRAWAL SUCCESS: " + denominationLongMap);
        } catch (Exception e) {
            display.printError(e.getMessage());
        }
        display.printText("after withdrawal: " + dispenser.balance(currency));
        return denominationLongMap;
    }

}
