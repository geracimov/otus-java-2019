package ru.geracimov.otus.java.thread;

public enum Direction {
    FORWARD(1), BACK(-1);

    private final int inc;

    Direction(int inc) {
        this.inc = inc;
    }

    public int getInc() {
        return inc;
    }

}
