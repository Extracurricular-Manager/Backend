package fr.periscol.backend.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "family")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refering_parent_name")
    private String referingParentName;

    @Column(name = "refering_parent_surname")
    private String referingParentSurname;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "postal_adress")
    private String postalAdress;
}
