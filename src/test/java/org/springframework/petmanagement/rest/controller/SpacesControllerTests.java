package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.service.DocumentService;
import org.springframework.petmanagement.service.SpaceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class SpacesControllerTests {

    private static final UUID SPACE_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID DOCUMENT_ID = UUID.fromString("20000000-0000-0000-0000-000000000002");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private SpaceService spaceService;
    @MockitoBean private DocumentService documentService;
    @Autowired private ObjectMapper objectMapper;

    private Space space;
    private Document document;
    private User user;

    @BeforeEach
    void initData() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        // Set up mock authentication
        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        space = new Space();
        space.setId(SPACE_ID);
        space.setUserId(user.getId());
        space.setName("Test Space");

        document = new Document();
        document.setId(DOCUMENT_ID);
        document.setSpaceId(SPACE_ID);
        document.setTitle("Test Document");
        document.setBody(Map.of("content", "Test content"));
    }



    @Test
    @WithMockUser
    void testListSpacesSuccess() throws Exception {
        given(this.spaceService.findAllByUserId(any())).willReturn(List.of(space));
        this.mockMvc.perform(get("/api/spaces").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAddSpaceSuccess() throws Exception {
        Space newSpace = new Space();
        newSpace.setId(UUID.randomUUID());
        newSpace.setName("New Space");
        given(this.spaceService.save(any(Space.class))).willReturn(newSpace);

        String jsonBody = """
            {
              "name": "New Space"
            }
            """;

        this.mockMvc.perform(post("/api/spaces")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testListDocumentsSuccess() throws Exception {
        given(this.documentService.findAllBySpaceId(SPACE_ID)).willReturn(List.of(document));
        this.mockMvc.perform(get("/api/spaces/{spaceId}/documents", SPACE_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testAddDocumentSuccess() throws Exception {
        Document newDocument = new Document();
        newDocument.setId(UUID.randomUUID());
        newDocument.setTitle("New Document");
        given(this.documentService.save(any(Document.class))).willReturn(newDocument);

        String jsonBody = """
            {
              "title": "New Document",
              "body": {"content": "New content"}
            }
            """;

        this.mockMvc.perform(post("/api/spaces/{spaceId}/documents", SPACE_ID)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testGetDocumentSuccess() throws Exception {
        given(this.documentService.findById(DOCUMENT_ID)).willReturn(document);
        this.mockMvc.perform(get("/api/spaces/{spaceId}/documents/{documentId}", SPACE_ID, DOCUMENT_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testUpdateDocumentSuccess() throws Exception {
        Document updatedDocument = new Document();
        updatedDocument.setId(DOCUMENT_ID);
        updatedDocument.setTitle("Updated Document");
        given(this.documentService.findById(DOCUMENT_ID)).willReturn(document);
        given(this.documentService.update(any(Document.class))).willReturn(updatedDocument);

        String jsonBody = """
            {
              "title": "Updated Document",
              "body": {"content": "Updated content"}
            }
            """;

        this.mockMvc.perform(patch("/api/spaces/{spaceId}/documents/{documentId}", SPACE_ID, DOCUMENT_ID)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteDocumentSuccess() throws Exception {
        given(this.documentService.findById(DOCUMENT_ID)).willReturn(document);
        this.mockMvc.perform(delete("/api/spaces/{spaceId}/documents/{documentId}", SPACE_ID, DOCUMENT_ID).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
