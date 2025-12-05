package org.springframework.petmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public class NamedEntity extends BaseEntity {

    @Column(name = "name", length = 30)
    @NotEmpty
    @Size(min = 1, max = 30)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}