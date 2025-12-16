package org.springframework.petmanagement.service.visitService;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.UserService;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractVisitServiceTests {

    @Autowired
    protected VisitService visitService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PetService petService;

    @Autowired
    protected ClinicService clinicService;

    @Test
    void shouldListVisitsEvenIfEmpty() {
        Page<Visit> page = visitService.listVisits(null, PageRequest.of(0, 10));
        assertThat(page).isNotNull();
    }

    @Test
    void shouldCreateVisit() {
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

        VisitFieldsDto visitDto = new VisitFieldsDto();
        visitDto.setPetId(pet.getId());
        visitDto.setClinicId(clinic.getId());
        visitDto.setVisitedOn(LocalDate.now());

        Visit saved = visitService.createVisit(visitDto);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVisitedOn()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldFindVisitById() {
        String uniqueId = UUID.randomUUID().toString();

        // Create and save user
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setUsername("testuser2-" + uniqueId);
        userDto.setPassword("password");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setFirstNameKana("テスト");
        userDto.setLastNameKana("ユーザー");
        userDto.setEmail("test2-" + uniqueId + "@example.com");
        userDto.setTelephone("090-1234-5679");
        User user = userService.createUser(userDto);

        // Create and save pet
        PetFieldsDto petDto = new PetFieldsDto();
        petDto.setName("Test Pet");
        petDto.setUserId(user.getId());
        petDto.setType(PetTypeDto.CAT);
        petDto.setBirthDate(LocalDate.now().minusYears(1));
        Pet pet = petService.createPet(petDto);

        // Create and save clinic
        ClinicFieldsDto clinicDto = new ClinicFieldsDto();
        clinicDto.setName("Test Clinic-" + uniqueId);
        clinicDto.setTelephone("03-1234-5678");
        Clinic clinic = clinicService.createClinic(clinicDto);

        VisitFieldsDto visitDto = new VisitFieldsDto();
        visitDto.setPetId(pet.getId());
        visitDto.setClinicId(clinic.getId());
        visitDto.setVisitedOn(LocalDate.now());

        Visit saved = visitService.createVisit(visitDto);
        assertThat(visitService.getVisit(saved.getId())).isNotNull();
    }
}
