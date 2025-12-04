package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.PetTypeMapper;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.advice.ExceptionControllerAdvice;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.service.ClinicServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes=ClinicServiceImpl.class) 
@WebAppConfiguration
class PetTypeRestControllerTests {

    private static final UUID TYPE_ID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID TYPE_ID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID TYPE_ID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID TYPE_ID_4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID TYPE_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private PetTypeRestController petTypeRestController;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @MockitoBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<PetType> petTypes;

    @BeforeEach
    void initPetTypes(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(petTypeRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
        petTypes = new ArrayList<>();

        PetType petType = new PetType();
        petType.setId(TYPE_ID_1);
        petType.setName("cat");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(TYPE_ID_2);
        petType.setName("dog");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(TYPE_ID_3);
        petType.setName("lizard");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(TYPE_ID_4);
        petType.setName("snake");
        petTypes.add(petType);
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeSuccessAsOwnerAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));
        this.mockMvc.perform(get("/api/pettypes/" + TYPE_ID_1)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(TYPE_ID_1.toString()))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetPetTypeSuccessAsVetAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypes.get(0)));
        this.mockMvc.perform(get("/api/pettypes/" + TYPE_ID_1)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(TYPE_ID_1.toString()))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeNotFound() throws Exception {
        given(this.clinicService.findPetTypeById(TYPE_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/pettypes/" + TYPE_ID_NOT_FOUND)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetAllPetTypesSuccessAsOwnerAdmin() throws Exception {
        Collection<PetType> filteredPetTypes = List.of(petTypes.get(1), petTypes.get(3));
        given(this.clinicService.findAllPetTypes()).willReturn(filteredPetTypes);
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(TYPE_ID_2.toString()))
            .andExpect(jsonPath("$.[0].name").value("dog"))
            .andExpect(jsonPath("$.[1].id").value(TYPE_ID_4.toString()))
            .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesSuccessAsVetAdmin() throws Exception {
        Collection<PetType> filteredPetTypes = List.of(petTypes.get(1), petTypes.get(3));
        given(this.clinicService.findAllPetTypes()).willReturn(filteredPetTypes);
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(TYPE_ID_2.toString()))
            .andExpect(jsonPath("$.[0].name").value("dog"))
            .andExpect(jsonPath("$.[1].id").value(TYPE_ID_4.toString()))
            .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesNotFound() throws Exception {
        petTypes.clear();
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeSuccess() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setId(null);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeFieldsDto(newPetType));
        this.mockMvc.perform(post("/api/pettypes")
            .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeError() throws Exception {
        PetTypeFieldsDto newPetTypeFields = new PetTypeFieldsDto();
        newPetTypeFields.name(null);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(newPetTypeFields);
        this.mockMvc.perform(post("/api/pettypes")
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeSuccess() throws Exception {
        given(this.clinicService.findPetTypeById(TYPE_ID_2)).willReturn(Optional.of(petTypes.get(1)));
        
        PetType updatedPetTypeModel = petTypes.get(1);
        updatedPetTypeModel.setName("dog I");
        
        PetTypeFieldsDto updatedPetTypeFields = new PetTypeFieldsDto();
        updatedPetTypeFields.name("dog I"); 
        
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(updatedPetTypeFields);
        
        this.mockMvc.perform(put("/api/pettypes/" + TYPE_ID_2)
            .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk()); 

        this.mockMvc.perform(get("/api/pettypes/" + TYPE_ID_2)
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(TYPE_ID_2.toString()))
            .andExpect(jsonPath("$.name").value("dog I"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeError() throws Exception {
        PetTypeFieldsDto newPetTypeFields = new PetTypeFieldsDto();
        newPetTypeFields.name("");
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(newPetTypeFields);
        this.mockMvc.perform(put("/api/pettypes/" + TYPE_ID_1)
            .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
     }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeSuccess() throws Exception {
        PetType petTypeToDelete = petTypes.get(0);
        given(this.clinicService.findPetTypeById(TYPE_ID_1)).willReturn(Optional.of(petTypeToDelete));
        this.mockMvc.perform(delete("/api/pettypes/" + TYPE_ID_1)
            .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeError() throws Exception {
        given(this.clinicService.findPetTypeById(TYPE_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(delete("/api/pettypes/" + TYPE_ID_NOT_FOUND)
            .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }
}