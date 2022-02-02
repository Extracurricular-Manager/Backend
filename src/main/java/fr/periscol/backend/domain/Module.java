package fr.periscol.backend.domain;

import javax.persistence.*;

@MappedSuperclass
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
