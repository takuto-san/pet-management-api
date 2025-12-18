package org.springframework.petmanagement.rest.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserService userService;

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

        this.mockMvc.perform(post("/api/users")
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

        this.mockMvc.perform(post("/api/users")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
