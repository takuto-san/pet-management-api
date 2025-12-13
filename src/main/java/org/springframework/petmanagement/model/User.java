package org.springframework.petmanagement.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.petmanagement.model.base.Time;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Time {
    
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
    
    @NotBlank
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank
    @Column(name = "last_name")
    private String lastName;
    
    @NotBlank
    @Column(name = "first_name_kana")
    private String firstNameKana;
    
    @NotBlank
    @Column(name = "last_name_kana")
    private String lastNameKana;
    
    @NotBlank
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @Size(max = 8)
    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "prefecture")
    private String prefecture;
    
    @Size(max = 80)
    @Column(name = "city")
    private String city;
    
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    
    @Size(max = 20)
    @Pattern(regexp = "^[0-9-]*$")
    @Column(name = "telephone", unique = true)
    private String telephone;
    
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