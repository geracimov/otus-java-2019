package ru.geracimov.otus.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberLogger.class);
    private final Object monitor = new Object();
    private final int firstStep;
    private final int lastStep;
    private Direction direction;
    private String lastStepName;

    public NumberLogger(int firstStep, int lastStep) {
        this.firstStep = firstStep;
        this.lastStep = lastStep;
        direction = Direction.FORWARD;
    }

    public void printNumber(int delay) {
        int step = firstStep;
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (monitor) {
                String threadName = Thread.currentThread().getName();
                if (!threadName.equals(lastStepName)) {
                    LOGGER.info("{}", step);
                    if (step == firstStep) direction = Direction.FORWARD;
                    if (step == lastStep) direction = Direction.BACK;
                    step = step + direction.getInc();
                    lastStepName = threadName;
                }
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

}
