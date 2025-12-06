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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.petmanagement.model.base.BaseEntity;

import java.util.UUID;

/**
 * Model representing a veterinary clinic.
 */
@Entity
@Table(name = "clinics")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Clinic extends BaseEntity {
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "website_url")
    private String websiteUrl;
    
    @Column(name = "opening_hours")
    private String openingHours;
    
    @Column(name = "note")
    private String note;
}
