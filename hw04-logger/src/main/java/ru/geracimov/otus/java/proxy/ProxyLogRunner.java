package ru.geracimov.otus.java.proxy;

import ru.geracimov.otus.java.BusinessLogic;
import ru.geracimov.otus.java.GreatBusinessLogic;

public class ProxyLogRunner {

    public static void main(String[] args) {
        BusinessLogic bl = IoC.createProxy(GreatBusinessLogic.class,
                new Class[]{BusinessLogic.class});

        bl.doSomething();
        bl.doSomethingAgain(999);
        bl.doSomethingAgainAndAgain(5, 7, "someLine");
        bl.doSomethingSecretly(new Object());
    }

}
