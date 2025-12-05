package org.springframework.petmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.petmanagement.model.base.Person;

import java.util.*;

@Entity
@Table(name = "users")
public class User extends Person {
        
    @Column(name = "username", length = 20, unique = true)
    @NotEmpty
    private String username;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "role", length = 20)
    @NotEmpty
    private String role = "user";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Pet> pets;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    protected Set<Pet> getPetsInternal() {
        if (this.pets == null) {
            this.pets = new HashSet<>();
        }
        return this.pets;
    }

    protected void setPetsInternal(Set<Pet> pets) {
        this.pets = pets;
    }

    public List<Pet> getPets() {
        List<Pet> sortedPets = new ArrayList<>(getPetsInternal());
        PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedPets);
    }

    public void setPets(List<Pet> pets) {
        this.pets = new HashSet<>(pets);
    }

    public void addPet(Pet pet) {
        getPetsInternal().add(pet);
        pet.setUser(this); 
    }

    public Pet getPet(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Pet pet : getPetsInternal()) {
            if (!ignoreNew || !pet.isNew()) {
                String compName = pet.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return pet;
                }
            }
        }
        return null;
    }

    public Pet getPet(UUID petId) {
        return getPetsInternal().stream()
                .filter(p -> p.getId() != null && p.getId().equals(petId))
                .findFirst()
                .orElse(null);
    }
}