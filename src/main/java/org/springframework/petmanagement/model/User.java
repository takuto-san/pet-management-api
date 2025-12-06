/*
 * Copyright 2002-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.petmanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.petmanagement.model.base.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model representing a user.
 */
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
    
    @Builder.Default
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
    
    @Builder.Default
    @Size(max = 20)
    @Column(name = "role")
    private String role = "user";
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pet> pets = new ArrayList<>();
}
