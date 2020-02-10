package ru.geracimov.otus.java.asm;

import ru.geracimov.otus.java.GreatBusinessLogic;

public class AsmRunner {

    public static void main(String[] args) {
        GreatBusinessLogic logic = new GreatBusinessLogic();


        System.out.println("1. Do nothing...");
        logic.doNothing();

        System.out.println("2. Do doNothingAgain...");
        logic.doNothingAgain(Integer.MIN_VALUE);

        System.out.println("3. Do doNothingAgainAndAgain...");
        final String customString = logic.doNothingAgainAndAgain(1, Integer.MAX_VALUE, "Custom string");
        System.out.println(customString);

        System.out.println("4. doNothingSecretly...");
        logic.doNothingSecretly(new Object());

    }

}
