package org.springframework.petmanagement.service.prescriptionService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.rest.dto.PrescriptionCategoryDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractPrescriptionServiceTests {

    @Autowired
    protected PrescriptionService prescriptionService;

    @Test
    void shouldFindAll() {
        assertThat(prescriptionService.listPrescriptions(PageRequest.of(0, 10))).isNotNull();
    }

    @Test
    void shouldCreatePrescription() {
        PrescriptionFieldsDto fields = new PrescriptionFieldsDto()
            .name("Test Prescription")
            .category(PrescriptionCategory.VACCINE);

        Prescription saved = prescriptionService.createPrescription(fields);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Prescription");
    }

    @Test
    void shouldFindPrescriptionById() {
        PrescriptionFieldsDto fields = new PrescriptionFieldsDto()
            .name("Test Prescription")
            .category(PrescriptionCategory.VACCINE);

        Prescription saved = prescriptionService.createPrescription(fields);
        assertThat(prescriptionService.getPrescription(saved.getId())).isPresent();
    }
}
