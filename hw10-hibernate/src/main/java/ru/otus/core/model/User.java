package ru.otus.core.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "USERS")
@EqualsAndHashCode(exclude = {"phoneDataSets", "addressDataSet"})
@ToString(of = {"id", "name"})
@FieldNameConstants
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.ALL})
    private List<PhoneDataSet> phoneDataSets;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.ALL})
    private AddressDataSet addressDataSet;

    public User(String name) {
        this.name = name;
        this.phoneDataSets = new ArrayList<>();
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.addressDataSet = addressDataSet;
        addressDataSet.setUser(this);
    }

    public void addPhoneDataSet(PhoneDataSet phoneDataSet) {
        this.phoneDataSets.add(phoneDataSet);
        phoneDataSet.setUser(this);
    }

    public void removePhoneDataSet(PhoneDataSet phoneDataSet) {
        if (phoneDataSets.contains(phoneDataSet)) {
            phoneDataSet.setUser(null);
            this.phoneDataSets.remove(phoneDataSet);
        }
    }

}
