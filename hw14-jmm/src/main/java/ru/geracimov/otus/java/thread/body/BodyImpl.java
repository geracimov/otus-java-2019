package ru.geracimov.otus.java.thread.body;

import ru.geracimov.otus.java.thread.dance.Dance;

public class BodyImpl implements Body {
    private final Leg left;
    private final Leg right;

    public BodyImpl(Leg left, Leg right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void letsDance(Dance dance) throws InterruptedException {
        left.setDance(dance);
        right.setDance(dance);

        right.start();
        left.start();

        left.join();
        right.join();
    }

}
