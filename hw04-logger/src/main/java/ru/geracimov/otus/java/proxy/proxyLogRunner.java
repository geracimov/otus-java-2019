package ru.geracimov.otus.java.proxy;

import ru.geracimov.otus.java.BusinessLogic;
import ru.geracimov.otus.java.GreatBusinessLogic;

public class proxyLogRunner {

    public static void main(String[] args) {

        Class[] interfaces = new Class[1];
        interfaces[0] = BusinessLogic.class;
        BusinessLogic bl = IoC.createProxy(GreatBusinessLogic.class, interfaces);

        bl.doSomething();
        bl.doSomethingAgain(999);
        bl.doSomethingAgainAndAgain(5, 7, "someLine");
        bl.doSomethingSecretly(new Object());
    }

}
