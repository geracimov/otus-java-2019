package ru.geracimov.otus.java.ms.restcontroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.geracimov.otus.java.ms.model.User;
import ru.geracimov.otus.java.ms.services.UserService;

import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class UserRestController {
    public static final String API_USER = "/api/user";
    private final UserService userService;

    @GetMapping(path = API_USER)
    public ResponseEntity<List<User>> getAllUsers() {
        return ok(userService.findAll());
    }

    @PostMapping(path = API_USER)
    public ResponseEntity<User> saveNewUser(@RequestBody User user) {
        final long id = userService.saveUser(user);
        final URI uriCreated = URI.create(String.format("%s/%d", API_USER, id));
        return created(uriCreated).body(user);
    }

}
