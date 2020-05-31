package ru.geracimov.otus.java.multiprocess.messagesystem.ms;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    String getRemoteAddress();

    boolean sendMessage(Message msg);

    boolean canHandle(MessageType msgType);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);

}
