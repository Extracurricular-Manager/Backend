package fr.periscol.backend.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@Table(name = "role")
public class Role {

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    @OneToMany
    private List<Permission> permissions;
}
