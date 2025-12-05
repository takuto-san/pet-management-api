package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.ManagementService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetRestControllerTests {

    private static final UUID PET_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PET_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final UUID PET_TYPE_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");
    private static final UUID PET_TYPE_ID_INVALID = UUID.fromString("30000000-0000-0000-0000-000000000003");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ManagementService managementService;

    @Autowired private ObjectMapper objectMapper;

    private Pet pet;
    private PetType petType;

    @BeforeEach
    void initPets() {
        petType = new PetType();
        petType.setId(PET_TYPE_ID);
        petType.setName("dog");

        pet = new Pet();
        pet.setId(PET_ID);
        pet.setName("Rosy");
        pet.setBirthDate(LocalDate.now());
        pet.setType(petType);
        
    }

    private PetFieldsDto createValidFieldsDto() {
        return new PetFieldsDto()
            .name("Rosy Updated")
            .birthDate(LocalDate.now())
            .sex("メス")
            .typeId(PET_TYPE_ID);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetSuccess() throws Exception {
        given(this.managementService.findPetById(PET_ID)).willReturn(Optional.of(pet));
        given(this.managementService.findPetTypeById(PET_TYPE_ID)).willReturn(Optional.of(petType));

        PetFieldsDto fieldsDto = createValidFieldsDto();
        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(put("/api/pets/{id}", PET_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetNotFound() throws Exception {
        given(this.managementService.findPetById(PET_ID_NOT_FOUND)).willReturn(Optional.empty());

        PetFieldsDto fieldsDto = createValidFieldsDto();
        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(put("/api/pets/{id}", PET_ID_NOT_FOUND)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetInvalidType() throws Exception {
        given(this.managementService.findPetById(PET_ID)).willReturn(Optional.of(pet));
        given(this.managementService.findPetTypeById(PET_TYPE_ID_INVALID)).willReturn(Optional.empty());

        PetFieldsDto fieldsDto = createValidFieldsDto().typeId(PET_TYPE_ID_INVALID);
        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(put("/api/pets/{id}", PET_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetSuccess() throws Exception {
        given(this.managementService.findPetById(PET_ID)).willReturn(Optional.of(pet));
        this.mockMvc.perform(get("/api/pets/{id}", PET_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetNotFound() throws Exception {
        given(this.managementService.findPetById(PET_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/pets/{id}", PET_ID_NOT_FOUND).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllPetsSuccess() throws Exception {
        given(this.managementService.findAllPets()).willReturn(Collections.singletonList(pet));
        this.mockMvc.perform(get("/api/pets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllPetsNotFound() throws Exception {
        given(this.managementService.findAllPets()).willReturn(Collections.emptyList());
        this.mockMvc.perform(get("/api/pets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetError() throws Exception {
        PetFieldsDto fieldsDto = new PetFieldsDto();
        fieldsDto.setName(null);
        
        String jsonBody = objectMapper.writeValueAsString(fieldsDto);
        this.mockMvc.perform(put("/api/pets/{id}", PET_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeletePetSuccess() throws Exception {
        given(this.managementService.findPetById(PET_ID)).willReturn(Optional.of(pet));
        this.mockMvc.perform(delete("/api/pets/{id}", PET_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeletePetError() throws Exception {
        given(this.managementService.findPetById(PET_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(delete("/api/pets/{id}", PET_ID_NOT_FOUND).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}