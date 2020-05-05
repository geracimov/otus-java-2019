package ru.geracimov.otus.java.multiprocess.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private long id;
    private String name;
    private String login;
    private String password;

}
