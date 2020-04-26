package ru.geracimov.otus.java.thread;

import ru.geracimov.otus.java.thread.body.Body;
import ru.geracimov.otus.java.thread.body.BodyImpl;
import ru.geracimov.otus.java.thread.body.Leg;
import ru.geracimov.otus.java.thread.body.LegSimple;
import ru.geracimov.otus.java.thread.dance.StepDance;

public class Disco {

    public static void main(String[] args) throws InterruptedException {
        Leg left = new LegSimple("leftLeg", 1);
        Leg right = new LegSimple("rightLeg", 1000);

        Body brain = new BodyImpl(left, right);
        brain.letsDance(new StepDance(1, 10));
    }

}
