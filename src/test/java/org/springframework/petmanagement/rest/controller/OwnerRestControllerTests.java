package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.OwnerMapper;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.rest.dto.OwnerDto;
import org.springframework.petmanagement.rest.dto.OwnerFieldsDto;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.service.ManagementService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerRestControllerTests {

    private static final UUID OWNER_ID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID OWNER_ID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID PET_ID_1 = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PET_TYPE_ID_2 = UUID.fromString("20000000-0000-0000-0000-000000000002");

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private OwnerMapper ownerMapper;
    @MockitoBean private ManagementService managementService;

    private List<OwnerDto> owners;

    @BeforeEach
    void initOwners() {
        owners = new ArrayList<>();

        OwnerDto owner1 = new OwnerDto()
            .id(OWNER_ID_1)
            .firstName("George").lastName("Franklin")
            .firstNameKana("ジョージ").lastNameKana("フランクリン")
            .email("george.franklin@example.com")
            .address("110 W. Liberty St.").city("Madison").telephone("0123456789")
            .addPetsItem(getTestPetWithIdAndName(PET_ID_1, "Rosy"));
        
        OwnerDto owner2 = new OwnerDto()
            .id(OWNER_ID_2)
            .firstName("Betty").lastName("Davis")
            .firstNameKana("ベティ").lastNameKana("デイビス")
            .email("betty.davis@example.com")
            .address("638 Cardinal Ave.").city("Sun Prairie").telephone("0123456789");

        owners.add(owner1);
        owners.add(owner2);
    }

    private PetDto getTestPetWithIdAndName(final UUID id, final String name) {
        PetTypeDto petType = new PetTypeDto();
        PetDto pet = new PetDto();
        pet.id(id).name(name).birthDate(LocalDate.now()).type(petType.id(PET_TYPE_ID_2).name("dog"));
        return pet;
    }

    private OwnerFieldsDto createOwnerFieldsDto(OwnerDto ownerDto) {
        return new OwnerFieldsDto()
            .firstName(ownerDto.getFirstName())
            .lastName(ownerDto.getLastName())
            .firstNameKana(ownerDto.getFirstNameKana())
            .lastNameKana(ownerDto.getLastNameKana())
            .email(ownerDto.getEmail())
            .address(ownerDto.getAddress())
            .city(ownerDto.getCity())
            .telephone(ownerDto.getTelephone());
    }
    
    private PetFieldsDto createPetFieldsDto(PetDto petDto) {
        return new PetFieldsDto()
            .name(petDto.getName())
            .birthDate(petDto.getBirthDate())
            .sex(petDto.getSex())
            .typeId(petDto.getType().getId());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerSuccess() throws Exception {
        given(this.managementService.findOwnerById(OWNER_ID_1)).willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));
        this.mockMvc.perform(get("/api/owners/" + OWNER_ID_1).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(OWNER_ID_1.toString()));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerNotFound() throws Exception {
        given(this.managementService.findOwnerById(OWNER_ID_2)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/owners/" + OWNER_ID_2).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersListSuccess() throws Exception {
        List<Owner> ownerEntities = (List<Owner>) ownerMapper.toOwners(owners);
        given(this.managementService.findOwnerByKana(eq("デイビス"), any()))
            .willReturn(List.of(ownerEntities.get(1))); 

        this.mockMvc.perform(get("/api/owners").param("lastNameKana", "デイビス").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateOwnerSuccess() throws Exception {
        given(this.managementService.findOwnerById(OWNER_ID_1))
            .willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));
        
        OwnerDto originalDto = owners.get(0);
        originalDto.setFirstName("George Updated");
        
        OwnerFieldsDto fieldsDto = createOwnerFieldsDto(originalDto);

        this.mockMvc.perform(put("/api/owners/" + OWNER_ID_1)
                .with(csrf())
                .content(objectMapper.writeValueAsString(fieldsDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteOwnerSuccess() throws Exception {
        given(this.managementService.findOwnerById(OWNER_ID_1)).willReturn(Optional.of(ownerMapper.toOwner(owners.get(0))));
        this.mockMvc.perform(delete("/api/owners/" + OWNER_ID_1).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}