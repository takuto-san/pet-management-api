package org.springframework.petmanagement.service.petTypeService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "postgres"})
class PetTypeServiceSpringDataJpaTests extends AbstractPetTypeServiceTests {
    
}