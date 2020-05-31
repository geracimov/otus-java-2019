package ru.geracimov.otus.java.multiprocess.messagesystem.ms;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(String name);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();

    MsClient getMsClient(Message message);

}

