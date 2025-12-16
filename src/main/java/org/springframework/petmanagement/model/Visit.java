/*
 * Copyright 2002-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.petmanagement.model;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.petmanagement.model.base.Time;
import org.springframework.petmanagement.model.type.Currency;
import org.springframework.petmanagement.model.type.VisitType;
import org.springframework.petmanagement.model.type.converter.CurrencyConverter;
import org.springframework.petmanagement.model.type.converter.VisitTypeConverter;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Model representing a veterinary visit.
 */
@Entity
@Table(name = "visits")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Visit extends Time {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "visited_on", nullable = false)
    private LocalDate visitedOn;

    @Column(name = "weight")
    private Float weight;

    @Convert(converter = VisitTypeConverter.class)
    @ColumnTransformer(write = "?::visit_type")
    @Column(name = "visit_type")
    private VisitType visitType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "next_due_on")
    private LocalDate nextDueOn;

    @Column(name = "total_fee")
    private Integer totalFee;

    @lombok.Builder.Default
    @Convert(converter = CurrencyConverter.class)
    @ColumnTransformer(write = "?::currency_type")
    @Column(name = "currency")
    private Currency currency = Currency.JPY;

    @Column(name = "note")
    private String note;
}
