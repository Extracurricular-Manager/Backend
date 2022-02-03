package fr.periscol.backend.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @Column(name = "name")
    private String name;

    public Permission(String name) {
        this.name = name;
    }

    protected Permission() {}
}
