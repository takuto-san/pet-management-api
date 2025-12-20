package org.springframework.petmanagement.service.authService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles({"spring-data-jpa", "postgres"})
@Transactional
class AuthServiceSpringDataJpaTests extends AbstractAuthServiceTests {
}
