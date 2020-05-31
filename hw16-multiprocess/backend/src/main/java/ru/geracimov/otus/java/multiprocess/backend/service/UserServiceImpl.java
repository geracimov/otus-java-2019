package ru.geracimov.otus.java.multiprocess.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.geracimov.otus.java.multiprocess.backend.config.BackendProperties;
import ru.geracimov.otus.java.multiprocess.backend.model.User;
import ru.geracimov.otus.java.multiprocess.backend.ms.Message;
import ru.geracimov.otus.java.multiprocess.backend.ms.MessageType;
import ru.geracimov.otus.java.multiprocess.backend.ms.MsClient;
import ru.geracimov.otus.java.multiprocess.backend.repository.UserRepository;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService/*, CommandLineRunner */{
//    private final Map<MessageType, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final MsClient msClient;
    private final BackendProperties beProps;


//    @Override
//    public void run(String... args) {
//        final InetSocketAddress callbackSocket = new InetSocketAddress(beProps.getHost(), beProps.getPort());
//        Message outMsg = msClient.produceMessage("registerer", callbackSocket, MessageType.CLIENT_REGISTER);
////        consumerMap.put(outMsg.getType(), System.out::println);
//        msClient.sendMessage(outMsg);
//    }

//    @PreDestroy
//    private void unregister() {
//        final InetSocketAddress callbackSocket = new InetSocketAddress(beProps.getHost(), beProps.getPort());
//        Message outMsg = msClient.produceMessage("registerer", callbackSocket, MessageType.CLIENT_UNREGISTER);
////        consumerMap.put(outMsg.getType(), System.out::println);
//        msClient.sendMessage(outMsg);
//    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

}
