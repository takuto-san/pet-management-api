package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.repository.UserItemRepository;

@Profile("spring-data-jpa")
public interface SpringDataUserItemRepository extends UserItemRepository, PagingAndSortingRepository<UserItem, UUID> {

    Page<UserItem> findAll(Pageable pageable);

    List<UserItem> findByUserId(UUID userId);
}
