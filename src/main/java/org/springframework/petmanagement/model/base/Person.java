package org.springframework.petmanagement.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends Time {

    @Column(name = "first_name")
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String lastName;

    @Column(name = "first_name_kana")
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String firstNameKana;

    @Column(name = "last_name_kana")
    @NotEmpty
    @Size(min = 1, max = 30)
    protected String lastNameKana;

    @Column(name = "email")
    @NotEmpty
    @Email
    protected String email;

    @Column(name = "postal_code", length = 8)
    protected String postalCode;

    @Column(name = "prefecture")
    protected String prefecture;

    @Column(name = "city")
    protected String city;

    @Column(name = "address")
    protected String address;

    @Column(name = "telephone")
    @Size(max = 20)
    @Pattern(regexp = "^[0-9-]*$", message = "Telephone number must contain only digits and hyphens")
    protected String telephone;
}
