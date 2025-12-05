package org.springframework.petmanagement.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", length = 20, unique = true)
    @NotEmpty
    @Size(min = 1, max = 20)
    private String username;

    @Column(name = "password", length = 255)
    @NotEmpty
    @Size(min = 1, max = 255)
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void prePersist() {
        if (enabled == null) {
            enabled = true;
        }
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = (roles != null) ? roles : new HashSet<>();
    }

    @JsonIgnore
    public void addRole(String roleName) {
        Role role = new Role();
        role.setRole(roleName);
        role.setUser(this);
        this.roles.add(role);
    }
}