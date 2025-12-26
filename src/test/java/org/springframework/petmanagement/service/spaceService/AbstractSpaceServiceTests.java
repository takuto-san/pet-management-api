package org.springframework.petmanagement.service.spaceService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.service.SpaceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractSpaceServiceTests {

    @Autowired
    protected SpaceService spaceService;

    @Test
    void shouldFindAllByUserIdEvenIfEmpty() {
        assertThat(spaceService.findAllByUserId(UUID.randomUUID())).isNotNull();
    }

    @Test
    void shouldCreateSpace() {
        Space space = Space.builder()
            .userId(UUID.randomUUID())
            .name("Test Space")
            .build();

        Space saved = spaceService.save(space);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Space");
    }

    @Test
    void shouldFindSpaceById() {
        Space space = Space.builder()
            .userId(UUID.randomUUID())
            .name("Test Space")
            .build();

        Space saved = spaceService.save(space);
        Space found = spaceService.findById(saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
}
