package ru.geracimov.otus.java.orm.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.orm.annotation.Id;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private final long id;
    private String name;
    private Short age;

}
