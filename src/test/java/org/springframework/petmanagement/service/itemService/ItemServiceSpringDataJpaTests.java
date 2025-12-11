package org.springframework.petmanagement.service.itemService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "postgres"})
class ItemServiceSpringDataJpaTests extends AbstractItemServiceTests {

}
