package org.springframework.petmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.UserDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserSuccess() throws Exception {
        UserFieldsDto userFields = new UserFieldsDto();
        userFields.setUsername("newuser");
        userFields.setPassword("password123");
        userFields.setEnabled(true);
        
        userFields.setRoles(List.of("OWNER_ADMIN"));

        String jsonBody = objectMapper.writeValueAsString(userFields);

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
        UserFieldsDto userFields = new UserFieldsDto();
        userFields.setUsername(""); // Empty -> 400 Bad Request
        userFields.setPassword("pass");
        userFields.setEnabled(true);

        String jsonBody = objectMapper.writeValueAsString(userFields);

        this.mockMvc.perform(post("/api/users")
            .with(csrf())
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}