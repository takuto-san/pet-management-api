package org.springframework.petmanagement.model;

import org.springframework.petmanagement.model.base.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
public class Clinic extends Time {
    
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