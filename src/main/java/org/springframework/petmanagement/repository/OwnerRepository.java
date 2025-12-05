package org.springframework.petmanagement.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.petmanagement.model.Owner;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface OwnerRepository extends ListCrudRepository<Owner, UUID> {

    List<Owner> findByLastNameKanaLikeAndFirstNameKanaLike(@Nullable String lastNameKana, @Nullable String firstNameKana);

    List<Owner> findByLastNameKanaLike(String lastNameKana);
    
    List<Owner> findByFirstNameKanaLike(String firstNameKana);

}