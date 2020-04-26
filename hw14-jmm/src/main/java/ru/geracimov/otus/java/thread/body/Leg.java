package ru.geracimov.otus.java.thread.body;

import ru.geracimov.otus.java.thread.dance.Dance;

public abstract class Leg extends Thread {
    protected abstract void setDance(Dance dance);

}
