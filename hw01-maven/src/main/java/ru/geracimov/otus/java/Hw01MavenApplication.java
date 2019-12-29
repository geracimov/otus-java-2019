package ru.geracimov.otus.java;

import ru.geracimov.otus.java.utils.IntUtils;
import ru.geracimov.otus.java.utils.IntUtilsGuavaImpl;

public class Hw01MavenApplication {

    public static void main(String[] args) {
        if (args.length == 0)
            return;

        IntUtils UTILS = new IntUtilsGuavaImpl();
        int num = UTILS.parseInt(args[0]);

        final double factorial = UTILS.factorial(num);
        System.out.format("factorial %d: %s\n", num, factorial);

        final boolean isPowerOfTwo = UTILS.isPowerOfTwo(num);
        System.out.format("isPowerOfTwo %d: %s\n", num, isPowerOfTwo);

        final int log2 = UTILS.log2ceiling(num);
        System.out.format("log2 %d: %s\n", num, log2);
    }

}
