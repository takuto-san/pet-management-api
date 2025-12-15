package org.springframework.petmanagement.rest.controller;

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
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
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
class ClinicControllerTests {

    private static final UUID CLINIC_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID CLINIC_ID_NOT_FOUND = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ClinicService clinicService;
    @Autowired private ObjectMapper objectMapper;

    private Clinic clinic;

    @BeforeEach
    void initClinic() {
        clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        clinic.setName("Test Clinic");
        clinic.setTelephone("03-1234-5678");
        clinic.setAddress("Tokyo");
    }

    private ClinicFieldsDto createValidFieldsDto() {
        return new ClinicFieldsDto()
            .name("Updated Clinic")
            .telephone("03-9876-5432")
            .address("Osaka");
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testListClinicsSuccess() throws Exception {
        given(this.clinicService.listClinics()).willReturn(java.util.List.of(clinic));
        this.mockMvc.perform(get("/api/clinics").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetClinicSuccess() throws Exception {
        given(this.clinicService.getClinic(CLINIC_ID)).willReturn(Optional.of(clinic));
        this.mockMvc.perform(get("/api/clinics/{id}", CLINIC_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetClinicNotFound() throws Exception {
        given(this.clinicService.getClinic(CLINIC_ID_NOT_FOUND)).willReturn(Optional.empty());
        this.mockMvc.perform(get("/api/clinics/{id}", CLINIC_ID_NOT_FOUND).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddClinicSuccess() throws Exception {
        Clinic newClinic = new Clinic();
        newClinic.setId(UUID.randomUUID());
        newClinic.setName("New Clinic");
        given(this.clinicService.createClinic(any(ClinicFieldsDto.class))).willReturn(newClinic);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(post("/api/clinics")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateClinicSuccess() throws Exception {
        given(this.clinicService.updateClinic(eq(CLINIC_ID), any(ClinicFieldsDto.class))).willReturn(clinic);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(put("/api/clinics/{id}", CLINIC_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateClinicNotFound() throws Exception {
        doThrow(new IllegalArgumentException("clinic not found")).when(clinicService).updateClinic(eq(CLINIC_ID_NOT_FOUND), any(ClinicFieldsDto.class));

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(put("/api/clinics/{id}", CLINIC_ID_NOT_FOUND)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateClinicErrorValidation() throws Exception {
        ClinicFieldsDto fieldsDto = new ClinicFieldsDto();

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);
        this.mockMvc.perform(put("/api/clinics/{id}", CLINIC_ID)
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteClinicSuccess() throws Exception {
        this.mockMvc.perform(delete("/api/clinics/{id}", CLINIC_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteClinicNotFound() throws Exception {
        doThrow(new IllegalArgumentException("clinic not found")).when(clinicService).deleteClinic(CLINIC_ID_NOT_FOUND);

        this.mockMvc.perform(delete("/api/clinics/{id}", CLINIC_ID_NOT_FOUND).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
