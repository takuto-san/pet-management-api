package org.springframework.petmanagement.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "types")
@AttributeOverride(name = "name", column = @Column(name = "name", length = 80))
public class PetType extends NamedEntity {

    @Override
    @Size(min = 1, max = 80)
    public String getName() {
        return super.getName();
    }
    
}