package ru.geracimov.otus.java.atmdepartament;

import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.atmdepartament.bank.ConfigServer;
import ru.geracimov.otus.java.atmdepartament.money.Currency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class AtmDepartmentImpl implements AtmDepartment {
    private final List<AtmBackend> atmList = new ArrayList<>();
    private final ConfigServer configServer;

    @Override
    public long getAtmCount() {
        return atmList.size();
    }

    @Override
    public Map<Currency, Long> getBalance() {
        return atmList.stream()
                .map(AtmBackend::getBalance)
                .flatMap(b -> b.entrySet().stream())
                .collect(currencyBalanceCollector());
    }

    @Override
    public void restoreAtmConfigurations() {
        configServer.resetAtmConfiguration(atmList);
    }

    @Override
    public void loadAtmConfigurations() {
        atmList.forEach(atm -> atm.apply(configServer.loadConfiguration(atm)));
    }

    @Override
    public void addAtm(AtmBackend atm) {
        atmList.add(atm);
    }

    @Override
    public void addAtm(Collection<AtmBackend> atmList) {
        this.atmList.addAll(atmList);
    }

    private Collector<Map.Entry<Currency, Long>, ?, Map<Currency, Long>> currencyBalanceCollector() {
        return toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum);
    }

}
