package org.springframework.petmanagement.rest.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.rest.dto.PrescriptionCategoryDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PrescriptionControllerTests {

    private static final UUID PRESCRIPTION_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private PrescriptionService prescriptionService;
    @Autowired private ObjectMapper objectMapper;

    private Prescription prescription;

    @BeforeEach
    void initPrescription() {
        prescription = new Prescription();
        prescription.setId(PRESCRIPTION_ID);
        prescription.setName("Test Prescription");
        prescription.setCategory(MedicationType.VACCINE);
        prescription.setForm("Tablet");
        prescription.setStrength("10mg");
        prescription.setNote("Test note");
    }

    private PrescriptionFieldsDto createValidFieldsDto() {
        return new PrescriptionFieldsDto()
            .category(PrescriptionCategoryDto.VACCINE)
            .name("New Prescription")
            .form("Injection")
            .strength("5ml")
            .note("New note");
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testListPrescriptionsSuccess() throws Exception {
        given(this.prescriptionService.listPrescriptions(any())).willReturn(org.springframework.data.domain.Page.empty());
        this.mockMvc.perform(get("/api/prescriptions").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddPrescriptionSuccess() throws Exception {
        Prescription newPrescription = new Prescription();
        newPrescription.setId(UUID.randomUUID());
        newPrescription.setName("New Prescription");
        given(this.prescriptionService.createPrescription(any(PrescriptionFieldsDto.class))).willReturn(newPrescription);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(post("/api/prescriptions")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddPrescriptionErrorValidation() throws Exception {
        PrescriptionFieldsDto fieldsDto = new PrescriptionFieldsDto();

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);
        this.mockMvc.perform(post("/api/prescriptions")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
