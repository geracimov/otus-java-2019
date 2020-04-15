package ru.geracimov.otus.java.web.storage.core.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    @JsonManagedReference("user:phoneDataSets")
    private List<PhoneDataSet> phoneDataSets;
    @JsonManagedReference("user:addressDataSet")
    private AddressDataSet addressDataSet;

    public User(String name) {
        this.name = name;
        this.phoneDataSets = new ArrayList<>();
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.addressDataSet = addressDataSet;
        if (addressDataSet != null) addressDataSet.setUser(this);
    }

    public void addPhoneDataSet(PhoneDataSet phoneDataSet) {
        this.phoneDataSets.add(phoneDataSet);
        if (phoneDataSet != null) phoneDataSet.setUser(this);
    }

    public void removePhoneDataSet(PhoneDataSet phoneDataSet) {
        if (phoneDataSets.contains(phoneDataSet)) {
            phoneDataSet.setUser(null);
            this.phoneDataSets.remove(phoneDataSet);
        }
    }

}
