package org.springframework.petmanagement.service;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.repository.OwnerRepository;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final PetTypeRepository petTypeRepository;

    public ClinicServiceImpl(
        PetRepository petRepository,
        OwnerRepository ownerRepository,
        PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Pet> findAllPets() throws DataAccessException {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public void deletePet(Pet pet) throws DataAccessException {
        petRepository.delete(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Owner> findAllOwners() throws DataAccessException {
        return ownerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOwner(Owner owner) throws DataAccessException {
        if (owner != null) {
            ownerRepository.delete(owner);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetType> findPetTypeById(UUID id) {
        return Optional.ofNullable(petTypeRepository.findById(id)); 
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<PetType> findAllPetTypes() throws DataAccessException {
        return petTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void savePetType(PetType petType) throws DataAccessException {
        petTypeRepository.save(petType);
    }

    @Override
    @Transactional
    public void deletePetType(PetType petType) throws DataAccessException {
        petTypeRepository.delete(petType);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Owner> findOwnerById(UUID id) throws DataAccessException {
        return ownerRepository.findById(id);    
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findPetById(UUID id) throws DataAccessException {
        return Optional.ofNullable(petRepository.findById(id));
    }

    @Override
    @Transactional
    public void savePet(Pet pet) throws DataAccessException {
        PetType petType = findPetTypeById(pet.getType().getId())
            .orElseThrow(() -> new ObjectRetrievalFailureException(
                PetType.class, 
                pet.getType().getId()
            ));
            
        pet.setType(petType);
        petRepository.save(pet);
    }

    @Override
    @Transactional
    public void saveOwner(Owner owner) throws DataAccessException {
        ownerRepository.save(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByKana(@Nullable String lastNameKana, @Nullable String firstNameKana) throws DataAccessException {
        return ownerRepository.findByLastNameKanaAndFirstNameKana(lastNameKana, firstNameKana);
    }
}