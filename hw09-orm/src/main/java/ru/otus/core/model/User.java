package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.jdbc.query.Id;

@Data
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private long id;
    private String name;
    private int age;

}
