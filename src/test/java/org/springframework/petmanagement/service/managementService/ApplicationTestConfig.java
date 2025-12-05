package org.springframework.petmanagement.service.managementService;

import jakarta.annotation.PostConstruct;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class ApplicationTestConfig {

	@PostConstruct
	public void initializeMocks(){
		MockitoAnnotations.openMocks(this);
	}

}