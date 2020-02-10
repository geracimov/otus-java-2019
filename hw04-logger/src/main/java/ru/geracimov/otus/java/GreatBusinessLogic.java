package ru.geracimov.otus.java;

public class GreatBusinessLogic {

    public GreatBusinessLogic() {

    }

    @Log
    public void doNothing() {

    }

    @Log
    public void doNothingAgain(int number) {

    }

    @Log
    public String doNothingAgainAndAgain(int number, int numberAgain, String line) {
        return String.format("Result is: %s-%d-%d", line, number, numberAgain);
    }

    public void doNothingSecretly(Object importantData) {

    }

}
