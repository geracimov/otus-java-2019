package ru.geracimov.otus.java;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GreatBusinessLogic implements BusinessLogic {
    final Random random;

    public GreatBusinessLogic() {
        random = new Random();
    }

    @Log
    public void doSomething() {
        System.out.println("doSomething logic started");
        hardWork(50, 100);
    }

    @Log
    public void doSomethingAgain(int number) {
        System.out.println("doSomethingAgain logic started");
        hardWork(100, 500);
    }

    @Log
    public String doSomethingAgainAndAgain(int number, int numberAgain, String line) {
        System.out.println("doSomethingAgainAndAgain logic started");
        hardWork(100, 500);
        return String.format("Result is: %s-%d-%d", line, number, numberAgain);
    }

    public void doSomethingSecretly(Object importantData) {
        System.out.println("doSomethingSecretly logic started");
        hardWork(500, 1000);
    }

    private void hardWork(int from, int to) {
        System.out.print("Do work...  ");
        final int millis = from + random.nextInt(to - from);
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("completed!");
    }

}
