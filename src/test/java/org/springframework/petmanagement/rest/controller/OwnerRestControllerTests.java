package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.OwnerMapper;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.advice.ExceptionControllerAdvice;
import org.springframework.petmanagement.rest.dto.OwnerDto;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.service.ClinicServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = ClinicServiceImpl.class)
@WebAppConfiguration
class OwnerRestControllerTests {

    private static final UUID OWNER_ID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID OWNER_ID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID OWNER_ID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID OWNER_ID_4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID PET_ID_1 = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PET_ID_2 = UUID.fromString("10000000-0000-0000-0000-000000000002");
    private static final UUID PET_TYPE_ID_2 = UUID.fromString("20000000-0000-0000-0000-000000000002");

    @Autowired
    private OwnerRestController ownerRestController;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<OwnerDto> owners;
    private List<PetDto> pets;

    @BeforeEach
    void initOwners() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
            .setControllerAdvice(new ExceptionControllerAdvice())
            .build();
        owners = new ArrayList<>();

        OwnerDto ownerWithPet = new OwnerDto();
        owners.add(ownerWithPet.id(OWNER_ID_1).firstName("George").lastName("Franklin")
                .address("110 W. Liberty St.").city("Madison").telephone("6085551023")
                .addPetsItem(getTestPetWithIdAndName(ownerWithPet, PET_ID_1, "Rosy")));
        OwnerDto owner = new OwnerDto();
        owners.add(owner.id(OWNER_ID_2).firstName("Betty").lastName("Davis")
                .address("638 Cardinal Ave.").city("Sun Prairie").telephone("6085551749"));
        owner = new OwnerDto();
        owners.add(owner.id(OWNER_ID_3).firstName("Eduardo").lastName("Rodriquez")
                .address("2693 Commerce St.").city("McFarland").telephone("6085558763"));
        owner = new OwnerDto();
        owners.add(owner.id(OWNER_ID_4).firstName("Harold").lastName("Davis")
                .address("563 Friendly St.").city("Windsor").telephone("6085553198"));

        PetTypeDto petType = new PetTypeDto();
        petType.id(PET_TYPE_ID_2)
            .name("dog");

        pets = new ArrayList<>();
        PetDto pet = new PetDto();
        pets.add(pet.id(PET_ID_1)
            .name("Rosy")
            .birthDate(LocalDate.now())
            .type(petType));
        pet = new PetDto();
        pets.add(pet.id(PET_ID_2)
            .name("Jewel")
            .birthDate(LocalDate.now())
            .type(petType));
    }

    private PetDto getTestPetWithIdAndName(final OwnerDto owner, final UUID id, final String name) {
        PetTypeDto petType = new PetTypeDto();
        PetDto pet = new PetDto();
        pet.id(id).name(name).birthDate(LocalDate.now()).type(petType.id(PET_TYPE_ID_2).name("dog"));
        return pet;
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(OWNER_ID_1)).willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));
        this.mockMvc.perform(get("/api/owners/" + OWNER_ID_1)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(OWNER_ID_1.toString()))
            .andExpect(jsonPath("$.firstName").value("George"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerNotFound() throws Exception {
        given(this.clinicService.findOwnerById(OWNER_ID_2)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/owners/" + OWNER_ID_2)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersListSuccess() throws Exception {
        List<OwnerDto> filteredOwnersDto = List.of(owners.get(1), owners.get(3));

        BDDMockito.given(this.clinicService.findOwnerByKana("デイビス", null))
            .willReturn(ownerMapper.toOwners(filteredOwnersDto));

        this.mockMvc.perform(get("/api/owners?lastNameKana=デイビス") 
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(OWNER_ID_2.toString()))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(OWNER_ID_4.toString()))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(OWNER_ID_1)).willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));
        UUID ownerId = owners.get(0).getId();

        OwnerDto updatedOwnerDto = owners.get(0);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String newOwnerAsJSON = objectMapper.writeValueAsString(updatedOwnerDto);

        this.mockMvc.perform(put("/api/owners/" + ownerId)
                .content(newOwnerAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(OWNER_ID_1)).willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));

        this.mockMvc.perform(delete("/api/owners/" + OWNER_ID_1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

}