package ru.geracimov.otus.java.atmdepartament.exception;

public class AtmMoneyNotEnoughException extends AtmException {
    private static final String EXCEPTION_TEMPLATE = "Cannot give out requested amount '%s', not enough '%s'";

    public AtmMoneyNotEnoughException(long amount, long notEnough) {
        super(String.format(EXCEPTION_TEMPLATE, amount, notEnough));
    }

}
