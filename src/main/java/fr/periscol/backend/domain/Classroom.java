package fr.periscol.backend.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "classroom")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "professor")
    private String professor;
}
