package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.otus.jdbc.query.Id;

import java.math.BigDecimal;

@Data
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

}
