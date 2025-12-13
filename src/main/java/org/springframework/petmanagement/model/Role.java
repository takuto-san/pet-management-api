package org.springframework.petmanagement.model;

import org.springframework.petmanagement.model.base.BaseEntity;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.petmanagement.model.type.converter.RoleTypeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "roles")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @NotNull
    @Convert(converter = RoleTypeConverter.class)
    @Column(name = "name", nullable = false, unique = true)
    private RoleType name;
}