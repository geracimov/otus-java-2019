package ru.geracimov.otus.java.db.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "street"})
@ToString(of = {"id", "street"})
@Table(name = "ADDRESS")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "NUMBER", length = 1024, nullable = false)
    private String street;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "addressDataSet", cascade = {CascadeType.ALL})
    private User user;

    public AddressDataSet(String street) {
        this.street = street;
    }

}
