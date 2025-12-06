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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Model representing a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    
    @NotBlank
    @Size(max = 20)
    private String username;
    
    @NotBlank
    @Size(max = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Builder.Default
    private Boolean enabled = true;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @NotBlank
    private String firstNameKana;
    
    @NotBlank
    private String lastNameKana;
    
    @NotBlank
    @Email
    private String email;
    
    @Size(max = 8)
    private String postalCode;
    
    private String prefecture;
    
    @Size(max = 80)
    private String city;
    
    @Size(max = 255)
    private String address;
    
    @Size(max = 20)
    @Pattern(regexp = "^[0-9-]*$")
    private String telephone;
    
    @Builder.Default
    @Size(max = 20)
    private String role = "user";
}
