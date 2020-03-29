package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.jdbc.query.Id;

@Data
@FieldNameConstants
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String name;
    private int age;

}
