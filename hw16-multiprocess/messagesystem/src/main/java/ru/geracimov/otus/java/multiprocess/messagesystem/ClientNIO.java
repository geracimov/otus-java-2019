package ru.geracimov.otus.java.multiprocess.messagesystem;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
public class ClientNIO {
    private static final int PORT = 8081;
    private static final String HOST = "localhost";
    private final ByteBuffer buffer;

    public ClientNIO() throws IOException {
        final InetSocketAddress remote = new InetSocketAddress(HOST, PORT);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(remote);
        buffer = ByteBuffer.allocate(128);
    }

    private void send(SocketChannel socketChannel, String request) throws IOException {
        buffer.put(request.getBytes());
        buffer.flip();
        log.info("sending to server");
        socketChannel.write(buffer);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        while (true) {
            log.info("waiting for response");
            if (selector.select() > 0) {
                if (processServerResponse(selector)) {
                    return;
                }
            }
        }
    }

    private boolean processServerResponse(Selector selector) throws IOException {
        log.info("something happened");
        ByteBuffer buffer = ByteBuffer.allocate(128);
        Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
            SelectionKey key = selectedKeys.next();
            StringBuilder stringBuilder = new StringBuilder();
            selectedKeys.remove();

            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                while (socketChannel.read(buffer) > 0) {
                    buffer.flip();
                    String responsePart = StandardCharsets.UTF_8.decode(buffer).toString();
                    stringBuilder.append(responsePart);
                    buffer.clear();
                }
                System.out.println(stringBuilder);
                return true;
            }
        }
        return false;
    }

}

