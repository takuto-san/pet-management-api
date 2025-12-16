package org.springframework.petmanagement.service.visitPrescriptionService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.PrescriptionCategoryDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.petmanagement.service.UserService;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractVisitPrescriptionServiceTests {

    @Autowired
    protected VisitPrescriptionService visitPrescriptionService;

    @Autowired
    protected VisitService visitService;

    @Autowired
    protected PrescriptionService prescriptionService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PetService petService;

    @Autowired
    protected ClinicService clinicService;

    @Test
    void shouldFindVisitPrescriptionsByVisitId() {
        String uniqueId = UUID.randomUUID().toString();

        // Create and save user
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("testuser-" + uniqueId);
        userDto.setPassword("password");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setFirstNameKana("テスト");
        userDto.setLastNameKana("ユーザー");
        userDto.setEmail("test-" + uniqueId + "@example.com");
        userDto.setTelephone("090-1234-5678");
        User user = userService.createUser(userDto);

        // Create and save pet
        PetFieldsDto petDto = new PetFieldsDto();
        petDto.setName("Test Pet");
        petDto.setUserId(user.getId());
        petDto.setType(PetTypeDto.DOG);
        petDto.setBirthDate(LocalDate.now().minusYears(1));
        Pet pet = petService.createPet(petDto);

        // Create and save clinic
        ClinicFieldsDto clinicDto = new ClinicFieldsDto();
        clinicDto.setName("Test Clinic-" + uniqueId);
        clinicDto.setTelephone("03-1234-5678");
        Clinic clinic = clinicService.createClinic(clinicDto);

        // Create and save visit
        VisitFieldsDto visitDto = new VisitFieldsDto();
        visitDto.setPetId(pet.getId());
        visitDto.setClinicId(clinic.getId());
        visitDto.setVisitedOn(LocalDate.now());
        Visit visit = visitService.createVisit(visitDto);

        List<VisitPrescription> result = visitPrescriptionService.listVisitPrescriptionsByVisitId(visit.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void shouldCreateVisitPrescription() {
        String uniqueId = UUID.randomUUID().toString();

        // Create and save prescription
        PrescriptionFieldsDto prescriptionDto = new PrescriptionFieldsDto();
        prescriptionDto.setName("Test Prescription");
        prescriptionDto.setCategory(PrescriptionCategoryDto.VACCINE);
        Prescription prescription = prescriptionService.createPrescription(prescriptionDto);

        // Create and save user
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("testuser-" + uniqueId);
        userDto.setPassword("password");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setFirstNameKana("テスト");
        userDto.setLastNameKana("ユーザー");
        userDto.setEmail("test-" + uniqueId + "@example.com");
        userDto.setTelephone("090-1234-5678");
        User user = userService.createUser(userDto);

        // Create and save pet
        PetFieldsDto petDto = new PetFieldsDto();
        petDto.setName("Test Pet");
        petDto.setUserId(user.getId());
        petDto.setType(PetTypeDto.DOG);
        petDto.setBirthDate(LocalDate.now().minusYears(1));
        Pet pet = petService.createPet(petDto);

        // Create and save clinic
        ClinicFieldsDto clinicDto = new ClinicFieldsDto();
        clinicDto.setName("Test Clinic-" + uniqueId);
        clinicDto.setTelephone("03-1234-5678");
        Clinic clinic = clinicService.createClinic(clinicDto);

        // Create and save visit
        VisitFieldsDto visitDto = new VisitFieldsDto();
        visitDto.setPetId(pet.getId());
        visitDto.setClinicId(clinic.getId());
        visitDto.setVisitedOn(LocalDate.now());
        Visit visit = visitService.createVisit(visitDto);

        VisitPrescriptionFieldsDto vpDto = new VisitPrescriptionFieldsDto();
        vpDto.setVisitId(visit.getId());
        vpDto.setPrescriptionId(prescription.getId());
        vpDto.setQuantity(1.0f);
        vpDto.setUnit("tablet");

        VisitPrescription saved = visitPrescriptionService.createVisitPrescription(vpDto);
        assertThat(saved.getId()).isNotNull();
    }
}
