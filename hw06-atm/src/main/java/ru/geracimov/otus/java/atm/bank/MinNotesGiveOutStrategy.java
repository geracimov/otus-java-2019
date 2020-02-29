package ru.geracimov.otus.java.atm.bank;

import ru.geracimov.otus.java.atm.exception.AtmMoneyNotEnoughException;
import ru.geracimov.otus.java.atm.money.Denomination;

import java.util.Map;
import java.util.TreeMap;

public class MinNotesGiveOutStrategy implements GiveOutCalculationStrategy {

    @Override
    public Map<Denomination, Long> calculate(Map<Denomination, Long> leftovers, long amount) {
        Map<Denomination, Long> result = new TreeMap<>();
        long rest = amount;
        TreeMap<Denomination, Long> leftoversSorted = new TreeMap<>(leftovers);
        for (Map.Entry<Denomination, Long> entry : leftoversSorted.entrySet()) {
            if (rest == 0) break;
            final int nominal = entry.getKey().getNominal();
            final long giveOutNominal = Math.min(rest / nominal, entry.getValue());
            if (giveOutNominal > 0)
                result.put(entry.getKey(), giveOutNominal);
            rest -= giveOutNominal * nominal;
        }
        if (rest != 0) {
            throw new AtmMoneyNotEnoughException(amount, rest);
        }
        return result;
    }

}
