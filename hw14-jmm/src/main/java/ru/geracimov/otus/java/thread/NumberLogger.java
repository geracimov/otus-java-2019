package ru.geracimov.otus.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberLogger.class);
    private final Object monitor = new Object();
    private final int firstStep;
    private final int lastStep;
    private Direction direction;

    public NumberLogger(int firstStep, int lastStep) {
        this.firstStep = firstStep;
        this.lastStep = lastStep;
        direction = Direction.FORWARD;
    }

    public void printNumber(int delay) {
        int step = firstStep;
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (monitor) {
                LOGGER.info("{}", step);
                if (step == firstStep) direction = Direction.FORWARD;
                if (step == lastStep) direction = Direction.BACK;
                step = step + direction.getInc();
                switchThread(monitor);
            }
            sleep(delay);
        }
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void switchThread(Object monitor) {
        try {
            monitor.notify();
            monitor.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
