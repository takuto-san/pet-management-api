package org.springframework.petmanagement.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.petmanagement.model.base.Person;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Person {
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank
    @Size(max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @lombok.Builder.Default
    @Column(name = "enabled")
    private Boolean enabled = true;

    // Override Person fields to add unique constraints
    @Override
    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    public String getEmail() { return super.getEmail(); }
    @Override
    public void setEmail(String email) { super.setEmail(email); }

    @Override
    @Size(max = 20)
    @Pattern(regexp = "^[0-9-]*$")
    @Column(name = "telephone", unique = true)
    public String getTelephone() { return super.getTelephone(); }
    @Override
    public void setTelephone(String telephone) { super.setTelephone(telephone); }
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @lombok.Builder.Default
    private Set<Role> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private List<Pet> pets = new ArrayList<>();
}
