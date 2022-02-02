package fr.periscol.backend.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "id")
    private String id;
}
