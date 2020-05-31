package ru.geracimov.otus.java.multiprocess.messagesystem.ms;

import java.net.InetSocketAddress;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    InetSocketAddress getRemoteAddress();

    boolean sendMessage(Message msg);

    boolean canHandle(MessageType msgType);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);

}
