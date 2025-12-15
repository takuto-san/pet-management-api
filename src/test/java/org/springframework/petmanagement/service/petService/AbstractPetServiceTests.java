package org.springframework.petmanagement.service.petService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.petmanagement.model.type.PetType;
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
    void shouldListPetTypes() {
        List<PetType> all = petTypeService.listPetTypes();
        assertThat(all).isNotNull();
        assertThat(all).hasSize(7);
    }

    @Test
    void shouldListPetsEvenIfEmpty() {
        assertThat(petService.listPets(PageRequest.of(0, 10))).isNotNull();
    }
}
