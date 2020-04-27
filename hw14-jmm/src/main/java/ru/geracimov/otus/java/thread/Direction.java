package ru.geracimov.otus.java.thread;

public enum Direction {
    FORWARD {
        @Override
        public int getInc() {
            return 1;
        }
    },
    BACK {
        @Override
        public int getInc() {
            return -1;
        }
    };

    public abstract int getInc();

}
