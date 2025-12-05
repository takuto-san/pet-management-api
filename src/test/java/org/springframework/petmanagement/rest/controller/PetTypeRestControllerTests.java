package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.PetTypeMapper;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.ManagementService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetTypeRestControllerTests {

    private static final UUID TYPE_ID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID TYPE_ID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID TYPE_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @MockitoBean
    private ManagementService managementService;

    private List<PetType> petTypes;

    @BeforeEach
    void initPetTypes(){
        petTypes = new ArrayList<>();

        PetType petType1 = new PetType();
        petType1.setId(TYPE_ID_1);
        petType1.setName("cat");
        petTypes.add(petType1);

        PetType petType2 = new PetType();
        petType2.setId(TYPE_ID_2);
        petType2.setName("dog");
        petTypes.add(petType2);
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeSuccessAsOwnerAdmin() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));
        
        this.mockMvc.perform(get("/api/pettypes/{id}", TYPE_ID_1)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(TYPE_ID_1.toString()))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetPetTypeSuccessAsVetAdmin() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));
        
        this.mockMvc.perform(get("/api/pettypes/{id}", TYPE_ID_1)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(TYPE_ID_1.toString()))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeNotFound() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_NOT_FOUND)).willReturn(Optional.empty());
        
        this.mockMvc.perform(get("/api/pettypes/{id}", TYPE_ID_NOT_FOUND)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetAllPetTypesSuccessAsOwnerAdmin() throws Exception {
        given(this.managementService.findAllPetTypes()).willReturn(petTypes);
        
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id").value(TYPE_ID_1.toString()))
            .andExpect(jsonPath("$.[0].name").value("cat"))
            .andExpect(jsonPath("$.[1].id").value(TYPE_ID_2.toString()));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesSuccessAsVetAdmin() throws Exception {
        given(this.managementService.findAllPetTypes()).willReturn(petTypes);
        
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesNotFound() throws Exception {
        given(this.managementService.findAllPetTypes()).willReturn(Collections.emptyList());
        
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeSuccess() throws Exception {
        PetTypeFieldsDto newPetTypeDto = new PetTypeFieldsDto();
        newPetTypeDto.setName("cat");
        String jsonBody = objectMapper.writeValueAsString(newPetTypeDto);
        
        this.mockMvc.perform(post("/api/pettypes")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeError() throws Exception {
        PetTypeFieldsDto newPetTypeDto = new PetTypeFieldsDto();
        newPetTypeDto.setName(null); // バリデーションエラー
        String jsonBody = objectMapper.writeValueAsString(newPetTypeDto);
        
        this.mockMvc.perform(post("/api/pettypes")
                .with(csrf()) // 追加: CSRFトークン
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeSuccess() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_2)).willReturn(Optional.of(petTypes.get(1)));
        
        PetTypeFieldsDto updatedPetTypeDto = new PetTypeFieldsDto();
        updatedPetTypeDto.setName("dog I"); 
        
        String jsonBody = objectMapper.writeValueAsString(updatedPetTypeDto);
        
        this.mockMvc.perform(put("/api/pettypes/{id}", TYPE_ID_2)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeError() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));

        PetTypeFieldsDto updatedPetTypeDto = new PetTypeFieldsDto();
        updatedPetTypeDto.setName("");
        
        String jsonBody = objectMapper.writeValueAsString(updatedPetTypeDto);
        
        this.mockMvc.perform(put("/api/pettypes/{id}", TYPE_ID_1)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
   }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeSuccess() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));
        
        this.mockMvc.perform(delete("/api/pettypes/{id}", TYPE_ID_1)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeError() throws Exception {
        given(this.managementService.findPetTypeById(TYPE_ID_NOT_FOUND)).willReturn(Optional.empty());
        
        this.mockMvc.perform(delete("/api/pettypes/{id}", TYPE_ID_NOT_FOUND)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}