package ru.geracimov.otus.java.web.storage.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString(exclude = {"user"})
@NoArgsConstructor
public class PhoneDataSet {
    private long id;
    private String number;
    @JsonBackReference("user:phoneDataSets")
    private User user;

    public PhoneDataSet(String number) {
        this.number = number;
    }

}
