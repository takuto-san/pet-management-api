package org.springframework.petmanagement.rest.controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PetRestControllerTests {

    private static final UUID PET_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PET_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final UUID PET_TYPE_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");
    private static final UUID PET_TYPE_ID_INVALID = UUID.fromString("30000000-0000-0000-0000-000000000003");
    private static final UUID USER_ID = UUID.fromString("30000000-0000-0000-0000-000000000001");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PetService petService;
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
            .typeId(PET_TYPE_ID)
            .userId(USER_ID);
    }

    // Create
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreatePetSuccess() throws Exception {
        given(this.petService.createPet(any(PetFieldsDto.class))).willReturn(pet);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(post("/api/pets")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreatePetInvalidType() throws Exception {
        given(this.petService.createPet(any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("pet type not found"));

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto().typeId(PET_TYPE_ID_INVALID));

        this.mockMvc.perform(post("/api/pets")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreatePetErrorValidation() throws Exception {
        PetFieldsDto fieldsDto = new PetFieldsDto();

        given(this.petService.createPet(any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("Validation failed"));

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(post("/api/pets")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    // Update
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetSuccess() throws Exception {
        given(this.petService.updatePet(eq(PET_ID), any(PetFieldsDto.class))).willReturn(pet);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

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
        given(this.petService.updatePet(eq(PET_ID_NOT_FOUND), any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("pet not found"));

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

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
        given(this.petService.updatePet(eq(PET_ID), any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("pet type not found"));

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto().typeId(PET_TYPE_ID_INVALID));

        this.mockMvc.perform(put("/api/pets/{id}", PET_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    // Get
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetSuccess() throws Exception {
        given(this.petService.findById(PET_ID)).willReturn(Optional.of(pet));
        this.mockMvc.perform(get("/api/pets/{id}", PET_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllPetsSuccess() throws Exception {
        given(this.petService.findAll()).willReturn(java.util.List.of(pet));
        this.mockMvc.perform(get("/api/pets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllPetsEmptyStillOk() throws Exception {
        given(this.petService.findAll()).willReturn(java.util.List.of());
        this.mockMvc.perform(get("/api/pets").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetPetNotFound() throws Exception {
        given(this.petService.findById(PET_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/pets/{id}", PET_ID_NOT_FOUND).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdatePetErrorValidation() throws Exception {
        PetFieldsDto fieldsDto = new PetFieldsDto();

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);
        this.mockMvc.perform(put("/api/pets/{id}", PET_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    // Delete
    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeletePetSuccess() throws Exception {
        this.mockMvc.perform(delete("/api/pets/{id}", PET_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeletePetNotFound() throws Exception {
        doThrow(new IllegalArgumentException("pet not found")).when(petService).deletePet(PET_ID_NOT_FOUND);

        this.mockMvc.perform(delete("/api/pets/{id}", PET_ID_NOT_FOUND).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}