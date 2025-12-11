package org.springframework.petmanagement.service.visitPrescriptionService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractVisitPrescriptionServiceTests {

    @Autowired
    protected VisitPrescriptionService visitPrescriptionService;

    @Test
    void shouldFindVisitPrescriptionsByVisitId() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");

        Pet pet = new Pet();
        pet.setName("Test Pet");

        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic");

        Visit visit = new Visit();
        visit.setVisitedOn(LocalDate.now());
        visit.setUser(user);
        visit.setPet(pet);
        visit.setClinic(clinic);

        Prescription prescription = new Prescription();
        prescription.setName("Test Prescription");
        prescription.setCategory(MedicationType.VACCINE);

        VisitPrescription vp = new VisitPrescription();
        vp.setVisit(visit);
        vp.setPrescription(prescription);
        vp.setQuantity(1.0f);
        vp.setUnit("tablet");

        List<VisitPrescription> result = visitPrescriptionService.findByVisitId(visit.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void shouldCreateVisitPrescription() {
        VisitPrescription vp = new VisitPrescription();
        vp.setQuantity(1.0f);
        vp.setUnit("tablet");

        VisitPrescription saved = visitPrescriptionService.save(vp);
        assertThat(saved.getId()).isNotNull();
    }
}