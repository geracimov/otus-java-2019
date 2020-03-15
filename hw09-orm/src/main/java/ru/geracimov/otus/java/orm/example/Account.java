package ru.geracimov.otus.java.orm.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geracimov.otus.java.orm.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

}
