package org.springframework.petmanagement.service.petTypeService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractPetTypeServiceTests {

    @Autowired
    protected PetTypeService petTypeService;

    @Test
    void shouldCreateAndListPetTypes() throws Exception {
        String uniqueName = "bird-" + UUID.randomUUID().toString();

        PetTypeFieldsDto dto = new PetTypeFieldsDto();
        setField(dto, "name", uniqueName);

        PetType created = petTypeService.create(dto);

        List<PetType> all = petTypeService.findAll();
        assertThat(all).extracting(PetType::getId).contains(created.getId());
        
        assertThat(created.getName()).isEqualTo(uniqueName);
    }

    @Test
    void shouldReturnEmptyListSafely() {
        List<PetType> all = petTypeService.findAll();
        assertThat(all).isNotNull();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}