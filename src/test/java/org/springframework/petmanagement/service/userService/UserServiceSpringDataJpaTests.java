package org.springframework.petmanagement.service.userService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "postgres"})
class UserServiceSpringDataJpaTests extends AbstractUserServiceTests {
    
}