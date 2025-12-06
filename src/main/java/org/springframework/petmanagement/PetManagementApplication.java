package org.springframework.petmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PetManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PetManagementApplication.class, args);
	}
}
