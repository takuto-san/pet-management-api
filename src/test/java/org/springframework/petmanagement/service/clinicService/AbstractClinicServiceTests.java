package org.springframework.petmanagement.service.clinicService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public abstract class AbstractClinicServiceTests {

    @Autowired
    protected ClinicService clinicService;

    @Test
    void shouldListClinicsEvenIfEmpty() {
        assertThat(clinicService.findAll()).isNotNull();
    }

    @Test
    void shouldCreateClinic() {
        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");
        clinic.setTelephone("03-1234-5678");

        Clinic saved = clinicService.save(clinic);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Clinic");
    }

    @Test
    void shouldFindClinicById() {
        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");
        clinic.setTelephone("03-1234-5678");

        Clinic saved = clinicService.save(clinic);
        assertThat(clinicService.findById(saved.getId())).isPresent();
    }
}
