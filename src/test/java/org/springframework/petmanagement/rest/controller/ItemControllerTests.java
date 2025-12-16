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
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.type.ItemType;
import org.springframework.petmanagement.rest.dto.ItemCategoryDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.service.ItemService;
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
class ItemControllerTests {

    private static final UUID ITEM_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");

    @Autowired private MockMvc mockMvc;
    @MockitoBean private ItemService itemService;
    @Autowired private ObjectMapper objectMapper;

    private Item item;

    @BeforeEach
    void initItem() {
        item = new Item();
        item.setId(ITEM_ID);
        item.setName("Test Item");
        item.setCategory(ItemType.MEDICAL);
        item.setNote("Test note");
    }

    private ItemFieldsDto createValidFieldsDto() {
        return new ItemFieldsDto()
            .category(ItemCategoryDto.FOOD)
            .name("New Item")
            .note("New note");
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testListItemsSuccess() throws Exception {
        given(this.itemService.listItems(any())).willReturn(org.springframework.data.domain.Page.empty());
        this.mockMvc.perform(get("/api/items").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddItemSuccess() throws Exception {
        Item newItem = new Item();
        newItem.setId(UUID.randomUUID());
        newItem.setName("New Item");
        given(this.itemService.createItem(any(ItemFieldsDto.class))).willReturn(newItem);

        String jsonBody = objectMapper.writeValueAsString(createValidFieldsDto());

        this.mockMvc.perform(post("/api/items")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddItemErrorValidation() throws Exception {
        ItemFieldsDto fieldsDto = new ItemFieldsDto();

        String jsonBody = objectMapper.writeValueAsString(fieldsDto);
        this.mockMvc.perform(post("/api/items")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
