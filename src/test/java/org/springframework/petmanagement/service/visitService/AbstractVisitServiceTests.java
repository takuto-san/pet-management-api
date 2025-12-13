package org.springframework.petmanagement.service.visitService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.PetTypeService;
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

    @Autowired
    protected PetTypeService petTypeService;

    @Test
    void shouldListVisitsEvenIfEmpty() {
        assertThat(visitService.findAll(null)).isNotNull();
    }

    @Test
    void shouldCreateVisit() throws Exception {
        // 一意な値を生成
        String uniqueId = UUID.randomUUID().toString();

        // Create and save user
        UserFieldsDto userDto = new UserFieldsDto();
        setField(userDto, "username", "testuser-" + uniqueId);
        setField(userDto, "password", "password");
        setField(userDto, "firstName", "Test");
        setField(userDto, "lastName", "User");
        setField(userDto, "firstNameKana", "テスト");
        setField(userDto, "lastNameKana", "ユーザー");
        setField(userDto, "email", "test-" + uniqueId + "@example.com");
        setField(userDto, "telephone", "090-1234-5678");
        setField(userDto, "enabled", true);
        User user = userService.createUser(userDto);

        // Create and save pet type
        PetTypeFieldsDto petTypeDto = new PetTypeFieldsDto();
        setField(petTypeDto, "name", "dog-" + uniqueId);
        PetType petType = petTypeService.create(petTypeDto);

        // Create and save pet
        PetFieldsDto petDto = new PetFieldsDto();
        setField(petDto, "name", "Test Pet");
        setField(petDto, "userId", user.getId());
        setField(petDto, "typeId", petType.getId());
        setField(petDto, "birthDate", LocalDate.now().minusYears(1));
        Pet pet = petService.createPet(petDto);

        // Create and save clinic
        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic-" + uniqueId);
        clinic.setTelephone("03-1234-5678");
        clinic.setAddress("Test Address");
        clinic = clinicService.save(clinic);

        Visit visit = new Visit();
        visit.setVisitedOn(LocalDate.now());
        visit.setUser(user);
        visit.setPet(pet);
        visit.setClinic(clinic);

        Visit saved = visitService.save(visit);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getVisitedOn()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldFindVisitById() throws Exception {
        String uniqueId = UUID.randomUUID().toString();

        // Create and save user
        UserFieldsDto userDto = new UserFieldsDto();
        setField(userDto, "username", "testuser2-" + uniqueId);
        setField(userDto, "password", "password");
        setField(userDto, "firstName", "Test");
        setField(userDto, "lastName", "User");
        setField(userDto, "firstNameKana", "テスト");
        setField(userDto, "lastNameKana", "ユーザー");
        setField(userDto, "email", "test2-" + uniqueId + "@example.com");
        setField(userDto, "telephone", "090-1234-5679");
        setField(userDto, "enabled", true);
        User user = userService.createUser(userDto);

        // Create and save pet type
        PetTypeFieldsDto petTypeDto = new PetTypeFieldsDto();
        setField(petTypeDto, "name", "cat-" + uniqueId);
        PetType petType = petTypeService.create(petTypeDto);

        // Create and save pet
        PetFieldsDto petDto = new PetFieldsDto();
        setField(petDto, "name", "Test Pet");
        setField(petDto, "userId", user.getId());
        setField(petDto, "typeId", petType.getId());
        setField(petDto, "birthDate", LocalDate.now().minusYears(1));
        Pet pet = petService.createPet(petDto);

        // Create and save clinic
        Clinic clinic = new Clinic();
        clinic.setName("Test Clinic-" + uniqueId);
        clinic.setTelephone("03-1234-5678");
        clinic.setAddress("Test Address");
        clinic = clinicService.save(clinic);

        Visit visit = new Visit();
        visit.setVisitedOn(LocalDate.now());
        visit.setUser(user);
        visit.setPet(pet);
        visit.setClinic(clinic);

        Visit saved = visitService.save(visit);
        assertThat(visitService.findById(saved.getId())).isPresent();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}