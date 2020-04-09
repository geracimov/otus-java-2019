package ru.otus.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "number"})
@ToString(of = {"id", "number"})
@Table(name = "PHONE")
@FieldNameConstants
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false)
    private long id;

    @Column(name = "NUMBER", length = 12, nullable = false)
    private String number;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "USER_ID")
    private User user;

    public PhoneDataSet(String number) {
        this.number = number;
    }

}
