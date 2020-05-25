package ru.geracimov.otus.java.multiprocess.backend.service.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.multiprocess.backend.service.server.Attachment;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
    private final static Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    @SneakyThrows
    public void completed(Integer result, Attachment attachment) {
        final AsynchronousSocketChannel channelClient = attachment.getChannelClient();
        if (result == -1) {
            channelClient.close();
            log.info("Stopped listening to client{}", attachment.getAddressClient());
            return;
        }

        final ByteBuffer buffer = attachment.getBuffer();
        if (attachment.isReadMode()) {
            buffer.flip();
/*            int limit = buffer.limit();
            byte[] bytes = new byte[limit];
            buffer.get(bytes, 0, limit);
            log.info("Client at {} sends message: {}", attachment.getAddressClient(), new String(bytes, UTF_8));*/

            final String s = UTF_8.decode(buffer).toString();
            System.out.println("UTF_8.decode(buffer).toString() = " + s);

//            if(new String(bytes).equals("end\r\n")){
            if(s.equals("end\r\n")){
                channelClient.close();
                log.info("Stopped listening to client{}", attachment.getAddressClient());
                return;
            }




            attachment.setReadMode(false);

  /*       //    buffer.rewind();
          buffer.clear();
            byte[] bytes2 = new byte[bytes.length * 2];
            System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
            System.arraycopy(bytes, 0, bytes2, bytes.length , bytes.length);
            buffer.put(bytes2);*/


            buffer.flip();
            channelClient.write(buffer, attachment, this);
        } else {
            attachment.setReadMode(true);
            buffer.clear();
            channelClient.read(buffer, attachment, this);
        }
    }

    @Override
    public void failed(Throwable t, Attachment att) {
        log.error("Connection with client broken");
    }
}