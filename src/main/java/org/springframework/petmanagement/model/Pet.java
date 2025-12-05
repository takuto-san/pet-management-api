package org.springframework.petmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import org.springframework.petmanagement.model.base.Time;

@Entity
@Table(name = "pets")
public class Pet extends Time { 

    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;
    
    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "sex", length = 10)
    @Size(max = 10)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    @NotNull
    private PetType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public PetType getType() {
        return this.type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNew() {
        return this.getId() == null;
    }
}