package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "child")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private ZonedDateTime birthday;

    @ManyToOne
    private Classroom classroom;

    @ManyToOne
    private Family adelphie;

    @ManyToOne
    private GradeLevel gradeLevel;

    @ManyToMany
    @JoinTable(name = "rel_child__diet", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "diet_id"))
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Set<Diet> diets = new HashSet<>();
}
