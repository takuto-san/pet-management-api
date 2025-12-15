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

import org.springframework.petmanagement.model.base.Time;
import org.springframework.petmanagement.model.type.MedicationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Model representing a prescription master record.
 */
@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Prescription extends Time {
    
    @NotNull
    @Column(name = "category", nullable = false)
    private MedicationType category;
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @Column(name = "form")
    private String form;
    
    @Column(name = "strength")
    private String strength;
    
    @Column(name = "note")
    private String note;
}
