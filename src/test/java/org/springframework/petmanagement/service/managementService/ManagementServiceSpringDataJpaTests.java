package org.springframework.petmanagement.service.managementService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.petmanagement.PetManagementApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = PetManagementApplication.class)
@ActiveProfiles({"spring-data-jpa", "postgres"})
@Sql(scripts = "classpath:db/postgres/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ManagementServiceSpringDataJpaTests extends AbstractManagementServiceTests {

}