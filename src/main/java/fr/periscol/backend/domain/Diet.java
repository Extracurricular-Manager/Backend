package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "diet")
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "diets")
    @JsonIgnoreProperties(value = { "classroom", "adelphie", "gradeLevel", "diets" }, allowSetters = true)
    private Set<Child> children = new HashSet<>();
}
