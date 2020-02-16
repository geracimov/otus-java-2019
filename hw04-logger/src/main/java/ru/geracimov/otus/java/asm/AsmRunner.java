package ru.geracimov.otus.java.asm;

import ru.geracimov.otus.java.GreatBusinessLogic;

public class AsmRunner {

    public static void main(String[] args) {
        GreatBusinessLogic logic = new GreatBusinessLogic();


        logic.doSomething();

        logic.doSomethingAgain(Integer.MIN_VALUE);

        logic.doSomethingAgainAndAgain(1, Integer.MAX_VALUE, "Custom string");

        logic.doSomethingSecretly(new Object());

    }

}
