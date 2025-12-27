package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.UserItem;

public interface UserItemRepository {

    void save(UserItem userItem) throws DataAccessException;

    void delete(UserItem userItem) throws DataAccessException;

    UserItem findById(UUID id) throws DataAccessException;

    Collection<UserItem> findAll() throws DataAccessException;

    Collection<UserItem> findByUserId(UUID userId) throws DataAccessException;

    Page<UserItem> findAll(Pageable pageable) throws DataAccessException;
}
