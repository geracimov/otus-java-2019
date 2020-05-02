package ru.geracimov.otus.java.thread;

import java.util.concurrent.CountDownLatch;

public class NumberSequence {

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        NumberLogger numberLogger = new NumberLogger(1, 10);
        Thread t1 = new Thread(() -> {
            try {
                latch.await();
                numberLogger.printNumber(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "firstThread");
        Thread t2 = new Thread(() -> {
            try {
                latch.await();
                numberLogger.printNumber(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "secondThread");

        t1.start();
        t2.start();

        latch.countDown();

        t1.join();
        t2.join();
    }

}
