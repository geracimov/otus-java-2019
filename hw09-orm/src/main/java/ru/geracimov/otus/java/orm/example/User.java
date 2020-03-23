package ru.geracimov.otus.java.orm.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.geracimov.otus.java.orm.annotation.Id;

@Data
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String name;
    private int age;

}
