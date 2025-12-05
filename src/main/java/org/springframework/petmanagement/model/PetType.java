package org.springframework.petmanagement.model;

import org.springframework.petmanagement.model.base.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "types")
public class PetType extends Time {

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