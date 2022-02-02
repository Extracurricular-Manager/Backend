package fr.periscol.backend.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "grade_level")
public class GradeLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "level")
    private String level;
}
