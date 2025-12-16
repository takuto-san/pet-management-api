package org.springframework.petmanagement.model;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.petmanagement.model.base.BaseEntity;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.petmanagement.model.type.converter.RoleTypeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "roles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @NotNull
    @Convert(converter = RoleTypeConverter.class)
    @ColumnTransformer(write = "?::role_type")
    @Column(name = "name", nullable = false, unique = true)
    private RoleType name;
}