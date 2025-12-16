package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.type.ItemType;
import org.springframework.petmanagement.repository.ItemRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.ItemRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataItemRepository extends ItemRepository, PagingAndSortingRepository<Item, UUID>, ItemRepositoryOverride {

    Page<Item> findAll(Pageable pageable);

    @Override
    @Query(value = "SELECT * FROM items WHERE category = CAST(:#{#category.name().toLowerCase()} AS item_category) ORDER BY name",
           nativeQuery = true)
    List<Item> findByCategory(@Param("category") ItemType category);
}
