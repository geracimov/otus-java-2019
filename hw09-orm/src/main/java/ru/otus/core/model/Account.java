package ru.otus.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.jdbc.query.Id;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Account {
    @Id
    private final long id;
    private final String type;
    private int rest;

}
