package org.springframework.petmanagement.service.petService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractPetServiceTests {

    @Autowired
    protected PetService petService;

    @Autowired
    protected PetTypeService petTypeService;

    @Test
    void shouldCreatePetTypeAndList() throws Exception {
        String uniqueName = "cat-" + UUID.randomUUID().toString();

        PetTypeFieldsDto dto = new PetTypeFieldsDto();
        set(dto, "name", uniqueName);
        
        PetType created = petTypeService.create(dto);

        List<PetType> all = petTypeService.findAll();
        assertThat(all).extracting(PetType::getId).contains(created.getId());
        
        assertThat(created.getName()).isEqualTo(uniqueName);
    }

    @Test
    void shouldListPetsEvenIfEmpty() {
        assertThat(petService.findAll()).isNotNull();
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}