package org.springframework.petmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

@MappedSuperclass
public class Person extends BaseEntity {

    @Column(name = "first_name", length = 30)
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String firstName;

    @Column(name = "last_name", length = 30)
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String lastName;

    @Column(name = "first_name_kana", length = 30)
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String firstNameKana;

    @Column(name = "last_name_kana", length = 30)
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String lastNameKana;

    @Column(name = "email", length = 255)
    @NotEmpty
    @Email
    protected String email;

    @Column(name = "created_at")
    protected OffsetDateTime createdAt;

    @Column(name = "updated_at")
    protected OffsetDateTime updatedAt;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstNameKana() {
        return firstNameKana;
    }

    public void setFirstNameKana(String firstNameKana) {
        this.firstNameKana = firstNameKana;
    }

    public String getLastNameKana() {
        return lastNameKana;
    }

    public void setLastNameKana(String lastNameKana) {
        this.lastNameKana = lastNameKana;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}