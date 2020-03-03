package ru.geracimov.otus.java.atm.money;

public enum Denomination {
    B5000(5000), B2000(2000), B1000(1000), B500(500), B200(200),
    B100(100), B50(50), B20(20), B10(10), B5(5), B2(2), B1(1);

    private final int nominal;

    Denomination(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

}
