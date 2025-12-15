package org.springframework.petmanagement.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.PetType;

class PetTypeTests {

    @Test
    void shouldHaveDogValue() {
        assertThat(PetType.DOG).isNotNull();
    }

    @Test
    void shouldHaveCatValue() {
        assertThat(PetType.CAT).isNotNull();
    }

    @Test
    void shouldHaveRabbitValue() {
        assertThat(PetType.RABBIT).isNotNull();
    }

    @Test
    void shouldHaveHamsterValue() {
        assertThat(PetType.HAMSTER).isNotNull();
    }

    @Test
    void shouldHaveBirdValue() {
        assertThat(PetType.BIRD).isNotNull();
    }

    @Test
    void shouldHaveTurtleValue() {
        assertThat(PetType.TURTLE).isNotNull();
    }

    @Test
    void shouldHaveFishValue() {
        assertThat(PetType.FISH).isNotNull();
    }

    @Test
    void shouldHaveAllExpectedValues() {
        PetType[] values = PetType.values();
        assertThat(values).hasSize(7);
        assertThat(values).contains(PetType.DOG, PetType.CAT, PetType.RABBIT, PetType.HAMSTER, PetType.BIRD, PetType.TURTLE, PetType.FISH);
    }
}
