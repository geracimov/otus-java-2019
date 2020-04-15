package ru.geracimov.otus.java.web.storage.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"user"})
@NoArgsConstructor
public class AddressDataSet {
    private long id;
    private String street;
    @JsonBackReference("user:addressDataSet")
    private User user;

    public AddressDataSet(String street) {
        this.street = street;
    }

}
