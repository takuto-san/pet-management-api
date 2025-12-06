package org.springframework.petmanagement.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@MappedSuperclass
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

    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstNameKana() { return firstNameKana; }
    public void setFirstNameKana(String firstNameKana) { this.firstNameKana = firstNameKana; }

    public String getLastNameKana() { return lastNameKana; }
    public void setLastNameKana(String lastNameKana) { this.lastNameKana = lastNameKana; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPrefecture() { return prefecture; }
    public void setPrefecture(String prefecture) { this.prefecture = prefecture; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}