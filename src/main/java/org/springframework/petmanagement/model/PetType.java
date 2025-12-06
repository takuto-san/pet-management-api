package org.springframework.petmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.springframework.petmanagement.model.base.BaseEntity;

@Entity
@Table(name = "types")
public class PetType extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(min = 1, max = 255)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}