package org.springframework.petmanagement.service.prescriptionService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractPrescriptionServiceTests {

    @Autowired
    protected PrescriptionService prescriptionService;

    @Test
    void shouldListPrescriptionsEvenIfEmpty() {
        assertThat(prescriptionService.findAll()).isNotNull();
    }

    @Test
    void shouldCreatePrescription() {
        Prescription prescription = new Prescription();
        prescription.setName("Test Prescription");
        prescription.setCategory(MedicationType.VACCINE);

        Prescription saved = prescriptionService.save(prescription);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Prescription");
    }

    @Test
    void shouldFindPrescriptionById() {
        Prescription prescription = new Prescription();
        prescription.setName("Test Prescription");
        prescription.setCategory(MedicationType.VACCINE);

        Prescription saved = prescriptionService.save(prescription);
        assertThat(prescriptionService.findById(saved.getId())).isPresent();
    }
}