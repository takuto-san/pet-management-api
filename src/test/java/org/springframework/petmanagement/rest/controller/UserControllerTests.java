package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetPageDto;
import org.springframework.petmanagement.rest.dto.PetSexDto;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.UserService;
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
class UserControllerTests {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;
    @MockitoBean private PetService petService;
    @MockitoBean private PetMapper petMapper;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserSuccess() throws Exception {
        User created = new User();
        created.setId(UUID.randomUUID());
        created.setUsername("newuser");
        given(this.userService.createUser(any(UserRegistrationDto.class))).willReturn(created);

        String jsonBody = """
            {
              "username": "newuser",
              "password": "Password123",
              "firstName": "太郎",
              "lastName": "山田",
              "firstNameKana": "タロウ",
              "lastNameKana": "ヤマダ",
              "email": "taro.yamada@example.com",
              "telephone": "090-1111-2222",
              "enabled": true
            }
            """;

        this.mockMvc.perform(post("/users")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserError() throws Exception {
        String jsonBody = """
            {
              "username": "",
              "password": "password123",
              "firstName": "太郎",
              "lastName": "山田",
              "firstNameKana": "タロウ",
              "lastNameKana": "ヤマダ",
              "email": "taro.yamada@example.com",
              "telephone": "090-1111-2222",
              "enabled": true
            }
            """;

        this.mockMvc.perform(post("/users")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "ADMIN")
    void testListPetsByUserSuccess() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");
        UUID petId = UUID.randomUUID();
        Pet pet = new Pet();
        pet.setId(petId);
        List<Pet> pets = List.of(pet);
        Page<Pet> petPage = new PageImpl<>(pets, PageRequest.of(0, 10), 1);

        given(userService.getUser(userId)).willReturn(new User());
        given(petService.listPetsByUser(userId, PageRequest.of(0, 10))).willReturn(petPage);
        given(petMapper.toPetPageDto(petPage)).willReturn(new PetPageDto());

        this.mockMvc.perform(get("/users/{userId}/pets", userId)
            .param("page", "0")
            .param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "87654321-4321-4321-4321-cba987654321", roles = "USER")
    void testListPetsByUserForbidden() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        given(userService.getUser(userId)).willReturn(new User());

        this.mockMvc.perform(get("/users/{userId}/pets", userId)
            .param("page", "0")
            .param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "ADMIN")
    void testListPetsByUserNotFound() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        given(userService.getUser(userId)).willThrow(new IllegalArgumentException("User not found"));

        this.mockMvc.perform(get("/users/{userId}/pets", userId)
            .param("page", "0")
            .param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "USER")
    void testUpdateUserSuccess() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");
        User updated = new User();
        updated.setId(userId);
        updated.setUsername("testuser");
        given(this.userService.updateUserBase(userId, any())).willReturn(updated);

        String jsonBody = """
            {
              "username": "testuser",
              "firstName": "太郎",
              "lastName": "山田",
              "email": "test@example.com",
              "profileIcon": "https://example.com/icon.png"
            }
            """;

        this.mockMvc.perform(put("/users/{userId}", userId)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "87654321-4321-4321-4321-cba987654321", roles = "USER")
    void testUpdateUserForbidden() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        this.mockMvc.perform(put("/users/{userId}", userId)
            .with(csrf())
            .content("{}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "USER")
    void testDeleteUserSuccess() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        this.mockMvc.perform(delete("/users/{userId}", userId)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "87654321-4321-4321-4321-cba987654321", roles = "USER")
    void testDeleteUserForbidden() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        this.mockMvc.perform(delete("/users/{userId}", userId)
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "USER")
    void testGetUserSuccess() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");
        User user = new User();
        user.setId(userId);
        given(this.userService.getUser(userId)).willReturn(user);

        this.mockMvc.perform(get("/users/{userId}", userId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "87654321-4321-4321-4321-cba987654321", roles = "USER")
    void testGetUserForbidden() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        this.mockMvc.perform(get("/users/{userId}", userId)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "CLINIC_ADMIN")
    void testCreatePetSuccess() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");
        Pet created = new Pet();
        created.setId(UUID.randomUUID());
        created.setName("Test Pet");

        PetFieldsDto fieldsDto = new PetFieldsDto()
            .name("Test Pet")
            .type(PetTypeDto.DOG);

        given(petService.createPet(any(PetFieldsDto.class))).willReturn(created);
        given(petMapper.toPetDto(created)).willReturn(new PetDto());

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(post("/users/{userId}/pets", userId)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "87654321-4321-4321-4321-cba987654321", roles = "USER")
    void testCreatePetForbidden() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        PetFieldsDto fieldsDto = new PetFieldsDto()
            .name("Test Pet")
            .type(PetTypeDto.DOG);

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(post("/users/{userId}/pets", userId)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "CLINIC_ADMIN")
    void testCreatePetUserNotFound() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        PetFieldsDto fieldsDto = new PetFieldsDto()
            .name("Test Pet")
            .type(PetTypeDto.DOG);

        given(petService.createPet(any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("User not found"));

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(post("/users/{userId}/pets", userId)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "12345678-1234-1234-1234-123456789abc", roles = "CLINIC_ADMIN")
    void testCreatePetTypeNotFound() throws Exception {
        UUID userId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        PetFieldsDto fieldsDto = new PetFieldsDto()
            .name("Test Pet")
            .type(PetTypeDto.DOG);

        given(petService.createPet(any(PetFieldsDto.class)))
            .willThrow(new IllegalArgumentException("Pet type not found"));

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);

        this.mockMvc.perform(post("/users/{userId}/pets", userId)
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
