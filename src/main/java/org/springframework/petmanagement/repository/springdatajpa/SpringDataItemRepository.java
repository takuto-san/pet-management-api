package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.type.ItemType;
import org.springframework.petmanagement.repository.ItemRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.ItemRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataItemRepository extends ItemRepository, Repository<Item, UUID>, ItemRepositoryOverride {
    
    @Override
    @Query("SELECT i FROM Item i WHERE i.category = :category ORDER BY i.name")
    List<Item> findByCategory(@Param("category") ItemType category);
}
