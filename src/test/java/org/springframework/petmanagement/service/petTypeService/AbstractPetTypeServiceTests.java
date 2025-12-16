package org.springframework.petmanagement.service.petTypeService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.service.PetTypeService;

public abstract class AbstractPetTypeServiceTests {

    @Autowired
    protected PetTypeService petTypeService;

    @Test
    void shouldListPetTypes() {
        assertThat(petTypeService.listPetTypes()).isNotNull();
    }
}
