package ru.geracimov.otus.java.thread.body;

import ru.geracimov.otus.java.thread.dance.Dance;

public class LegSimple extends Leg {
    private final int delay;
    private Dance dance;

    public LegSimple(String name, int delay) {
        super();
        this.delay = delay;
        setName(name);
    }

    @Override
    protected void setDance(Dance dance) {
        this.dance = dance;
    }

    @Override
    public void run() {
        dance.doDance(getName(), delay);
    }

}
