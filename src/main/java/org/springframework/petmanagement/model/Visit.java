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

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Model representing a veterinary visit.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    
    @NotNull
    private UUID id;
    
    @NotNull
    private UUID userId;
    
    @NotNull
    private UUID petId;
    
    @NotNull
    private UUID clinicId;
    
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitedOn;
    
    private Float weight;
    
    private VisitType visitType;
    
    private String reason;
    
    private String diagnosis;
    
    private String treatment;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextDueOn;
    
    private Integer totalFee;
    
    @Builder.Default
    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency = "JPY";
    
    private String note;
}
