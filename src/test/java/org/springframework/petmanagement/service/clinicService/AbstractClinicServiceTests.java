package org.springframework.petmanagement.service.clinicService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractClinicServiceTests {

    @Autowired
    protected ClinicService clinicService;

    @Test
    void shouldListClinicsEvenIfEmpty() {
        assertThat(clinicService.listClinics()).isNotNull();
    }

    @Test
    void shouldCreateClinic() {
        ClinicFieldsDto fields = new ClinicFieldsDto()
            .name("Test Clinic")
            .telephone("03-1234-5678");

        Clinic saved = clinicService.createClinic(fields);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Clinic");
    }

    @Test
    void shouldFindClinicById() {
        ClinicFieldsDto fields = new ClinicFieldsDto()
            .name("Test Clinic")
            .telephone("03-1234-5678");

        Clinic saved = clinicService.createClinic(fields);
        assertThat(clinicService.getClinic(saved.getId())).isPresent();
    }
}
