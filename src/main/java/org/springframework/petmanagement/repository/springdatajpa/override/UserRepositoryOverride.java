package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.User;

@Profile("spring-data-jpa")
public interface UserRepositoryOverride {

    void delete(User user) throws DataAccessException;

    Page<User> findByNameKana(String lastNameKana, String firstNameKana, Pageable pageable);

}
