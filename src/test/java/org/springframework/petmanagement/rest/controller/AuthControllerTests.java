package org.springframework.petmanagement.rest.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private AuthService authService;

    @Test
    void testAuthenticateUserSuccess() throws Exception {
        JwtResponseDto response = new JwtResponseDto("access-token", "Bearer", "refresh-token", null, "test@example.com", null);
        given(this.authService.authenticateUser(any())).willReturn(response);

        String jsonBody = """
            {
              "email": "test@example.com",
              "password": "password123"
            }
            """;

        this.mockMvc.perform(post("/api/auth/signin")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String jsonBody = """
            {
              "username": "newuser",
              "email": "newuser@example.com",
              "password": "password123"
            }
            """;

        this.mockMvc.perform(post("/api/auth/signup")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void testLogoutUserSuccess() throws Exception {
        this.mockMvc.perform(post("/api/auth/signout")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        JwtResponseDto refreshResponse = new JwtResponseDto("new-access-token", "Bearer", "new-refresh-token", null, "test@example.com", null);
        given(this.authService.refreshToken(any())).willReturn(refreshResponse);

        String jsonBody = """
            {
              "refreshToken": "old-refresh-token"
            }
            """;

        this.mockMvc.perform(post("/api/auth/refreshtoken")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testAuthenticateUserBadRequest() throws Exception {
        String jsonBody = """
            {
              "email": "",
              "password": "password123"
            }
            """;

        this.mockMvc.perform(post("/api/auth/signin")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
