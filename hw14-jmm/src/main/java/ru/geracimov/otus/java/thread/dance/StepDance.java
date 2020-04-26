package ru.geracimov.otus.java.thread.dance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepDance implements Dance {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepDance.class);
    private final int firstStep;
    private final int lastStep;
    private final Object monitor = new Object();
    private Direction direction;
    private String lastStepName;

    public StepDance(int firstStep, int lastStep) {
        this.firstStep = firstStep;
        this.lastStep = lastStep;
        direction = Direction.FORWARD;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void doDance(String name, int delay) {
        assert name != null;
        int step = firstStep;
        while (true) {
            synchronized (monitor) {
                if (!name.equals(lastStepName)) {
                    LOGGER.info("{}", step);
                    if (step == firstStep) direction = Direction.FORWARD;
                    if (step == lastStep) direction = Direction.BACK;
                    step = step + direction.getInc();
                    lastStepName = name;
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
