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
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.rest.dto.VisitTypeDto;
import org.springframework.petmanagement.service.VisitService;
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
class VisitControllerTests {

    private static final UUID VISIT_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID VISIT_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");
    private static final UUID PET_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");
    private static final UUID CLINIC_ID = UUID.fromString("40000000-0000-0000-0000-000000000004");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private VisitService visitService;
    @Autowired private ObjectMapper objectMapper;

    private Visit visit;
    private Pet pet;
    private Clinic clinic;

    @BeforeEach
    void initVisit() {
        pet = new Pet();
        pet.setId(PET_ID);
        pet.setName("Test Pet");

        clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        clinic.setName("Test Clinic");

        visit = new Visit();
        visit.setId(VISIT_ID);
        visit.setVisitedOn(LocalDate.now());
        visit.setPet(pet);
        visit.setClinic(clinic);
    }

    private VisitFieldsDto createValidFieldsDto() {
        return new VisitFieldsDto()
            .visitedOn(LocalDate.now())
            .petId(PET_ID)
            .clinicId(CLINIC_ID)
            .visitType(VisitTypeDto.CHECKUP)
            .weight(5.0f)
            .totalFee(5000);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testListVisitsSuccess() throws Exception {
        given(this.visitService.listVisits(eq(PET_ID), any())).willReturn(org.springframework.data.domain.Page.empty());
        this.mockMvc.perform(get("/api/visits").param("petId", PET_ID.toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetVisitSuccess() throws Exception {
        given(this.visitService.getVisit(VISIT_ID)).willReturn(Optional.of(visit));
        this.mockMvc.perform(get("/api/visits/{id}", VISIT_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetVisitNotFound() throws Exception {
        given(this.visitService.getVisit(VISIT_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/visits/{id}", VISIT_ID_NOT_FOUND).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testAddVisitSuccess() throws Exception {
        Visit newVisit = new Visit();
        newVisit.setId(UUID.randomUUID());
        newVisit.setVisitedOn(LocalDate.now());
        given(this.visitService.createVisit(any(VisitFieldsDto.class))).willReturn(newVisit);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(post("/api/visits")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateVisitSuccess() throws Exception {
        given(this.visitService.updateVisit(eq(VISIT_ID), any(VisitFieldsDto.class))).willReturn(visit);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(put("/api/visits/{id}", VISIT_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateVisitNotFound() throws Exception {
        given(this.visitService.updateVisit(eq(VISIT_ID_NOT_FOUND), any(VisitFieldsDto.class))).willThrow(new IllegalArgumentException("visit not found"));

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(put("/api/visits/{id}", VISIT_ID_NOT_FOUND)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVisitSuccess() throws Exception {
        this.mockMvc.perform(delete("/api/visits/{id}", VISIT_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteVisitNotFound() throws Exception {
        doThrow(new IllegalArgumentException("visit not found")).when(visitService).deleteVisit(VISIT_ID_NOT_FOUND);

        this.mockMvc.perform(delete("/api/visits/{id}", VISIT_ID_NOT_FOUND).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
