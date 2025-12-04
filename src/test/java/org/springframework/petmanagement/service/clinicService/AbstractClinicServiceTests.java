/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.petmanagement.service.clinicService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.*;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.petmanagement.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractClinicServiceTests {

private static final UUID OWNER_ID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID OWNER_ID_6 = UUID.fromString("00000000-0000-0000-0000-000000000006");
    private static final UUID PET_ID_7 = UUID.fromString("10000000-0000-0000-0000-000000000007");
    private static final UUID PET_ID_1 = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID PET_ID_3 = UUID.fromString("10000000-0000-0000-0000-000000000003");
    private static final UUID OWNER_ID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID PET_TYPE_ID_2 = UUID.fromString("20000000-0000-0000-0000-000000000002");
    private static final UUID PET_TYPE_ID_4 = UUID.fromString("20000000-0000-0000-0000-000000000004");
    private static final UUID PET_TYPE_ID_3 = UUID.fromString("20000000-0000-0000-0000-000000000003");
    private static final UUID PET_TYPE_ID_1 = UUID.fromString("20000000-0000-0000-0000-000000000001");

    @Autowired
    protected ClinicService clinicService;

    @Test
    void shouldFindOwnersByLastName() {
        Collection<Owner> owners = this.clinicService.findOwnerByKana("デービス", null);
        assertThat(owners.size()).isEqualTo(2);

        owners = this.clinicService.findOwnerByKana("なし", null);
        assertThat(owners.isEmpty()).isTrue();
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = this.clinicService.findOwnerById(OWNER_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_1.toString())
        );
        assertThat(owner.getLastName()).startsWith("Franklin");
        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().iterator().next().getType()).isNotNull();
        assertThat(owner.getPets().iterator().next().getType().getName()).isEqualTo("cat");
    }

    @Test
    @Transactional
    void shouldInsertOwner() {
        Collection<Owner> owners = this.clinicService.findOwnerByKana("シュルツ", null);
        int found = owners.size();

        Owner owner = new Owner();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setFirstNameKana("サム");
        owner.setLastNameKana("シュルツ");
        owner.setEmail("sam.schultz@example.com");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        
        this.clinicService.saveOwner(owner);
        assertThat(owner.getId()).isNotNull();
        assertThat(owner.getPet("null value")).isNull();
        
        owners = this.clinicService.findOwnerByKana("Schultz", null);
        assertThat(owners).hasSize(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdateOwner() {
        Owner owner = this.clinicService.findOwnerById(OWNER_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_1.toString())
        );
        String oldLastName = owner.getLastName();
        String newLastName = oldLastName + "X";

        owner.setLastName(newLastName);
        this.clinicService.saveOwner(owner);

        Owner updatedOwner = this.clinicService.findOwnerById(OWNER_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_1.toString())
        );
        assertThat(updatedOwner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindPetWithCorrectId() {
        Pet pet7 = this.clinicService.findPetById(PET_ID_7).orElseThrow(
            () -> new ObjectRetrievalFailureException(Pet.class, PET_ID_7.toString())
        );
        assertThat(pet7.getName()).startsWith("Samantha");
        assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean"); 
    }

    @Test
    @Transactional
    void shouldInsertPetIntoDatabaseAndGenerateId() {
        Owner owner6 = this.clinicService.findOwnerById(OWNER_ID_6).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_6.toString())
        );
        int found = owner6.getPets().size();

        Pet pet = new Pet();
        pet.setName("bowser");
        pet.setSex("male");
        
        Collection<PetType> types = this.clinicService.findAllPetTypes();
        PetType petType = EntityUtils.getById(types, PetType.class, PET_TYPE_ID_2);
        
        pet.setType(petType);
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets()).hasSize(found + 1);

        this.clinicService.savePet(pet);
        this.clinicService.saveOwner(owner6);

        owner6 = this.clinicService.findOwnerById(OWNER_ID_6).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_6.toString())
        );
        assertThat(owner6.getPets()).hasSize(found + 1);
        assertThat(pet.getId()).isNotNull();
    }

    @Test
    @Transactional
    void shouldUpdatePetName() throws Exception {
        Pet pet7 = this.clinicService.findPetById(PET_ID_7).orElseThrow(
            () -> new ObjectRetrievalFailureException(Pet.class, PET_ID_7.toString())
        );
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        this.clinicService.savePet(pet7);

        Pet updatedPet = this.clinicService.findPetById(PET_ID_7).orElseThrow(
            () -> new ObjectRetrievalFailureException(Pet.class, PET_ID_7.toString())
        );
        assertThat(updatedPet.getName()).isEqualTo(newName);
    }
    
    @Test
    void shouldFindAllPets(){
        Collection<Pet> pets = this.clinicService.findAllPets();
        Pet pet1 = EntityUtils.getById(pets, Pet.class, PET_ID_1);
        assertThat(pet1.getName()).isEqualTo("Leo");
        Pet pet3 = EntityUtils.getById(pets, Pet.class, PET_ID_3);
        assertThat(pet3.getName()).isEqualTo("Rosy");
    }

    @Test
    @Transactional
    void shouldDeletePet(){
        Pet pet = this.clinicService.findPetById(PET_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(Pet.class, PET_ID_1.toString())
        );
        this.clinicService.deletePet(pet);
        
        Optional<Pet> deletedPet = this.clinicService.findPetById(PET_ID_1);
        assertThat(deletedPet).isEmpty();
    }

    @Test
void shouldFindAllOwners(){
        Collection<Owner> owners = this.clinicService.findAllOwners();
        Owner owner1 = EntityUtils.getById(owners, Owner.class, OWNER_ID_1);
        assertThat(owner1.getFirstName()).isEqualTo("George");
        Owner owner3 = EntityUtils.getById(owners, Owner.class, OWNER_ID_3); 
        assertThat(owner3.getFirstName()).isEqualTo("Eduardo");
    }

    @Test
    @Transactional
    void shouldDeleteOwner(){
        Owner owner = this.clinicService.findOwnerById(OWNER_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(Owner.class, OWNER_ID_1.toString())
        );
        this.clinicService.deleteOwner(owner);
        
        Optional<Owner> deletedOwner = this.clinicService.findOwnerById(OWNER_ID_1);
        assertThat(deletedOwner).isEmpty();
    }

    @Test
    void shouldFindPetTypeById(){
        PetType petType = this.clinicService.findPetTypeById(PET_TYPE_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(PetType.class, PET_TYPE_ID_1.toString())
        );
        assertThat(petType.getName()).isEqualTo("cat");
    }

    @Test
    void shouldFindAllPetTypes(){
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, PET_TYPE_ID_1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType3 = EntityUtils.getById(petTypes, PetType.class, PET_TYPE_ID_3);
        assertThat(petType3.getName()).isEqualTo("lizard");
    }

    @Test
    @Transactional
    void shouldInsertPetType() {
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        int found = petTypes.size();

        PetType petType = new PetType();
        petType.setName("tiger");

        this.clinicService.savePetType(petType);
        assertThat(petType.getId()).isNotNull();

        petTypes = this.clinicService.findAllPetTypes();
        assertThat(petTypes).hasSize(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdatePetType(){
        PetType petType = this.clinicService.findPetTypeById(PET_TYPE_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(PetType.class, PET_TYPE_ID_1.toString())
        );
        String oldName = petType.getName();
        String newName = oldName + "X";
        petType.setName(newName);
        this.clinicService.savePetType(petType);
        PetType updatedPetType = this.clinicService.findPetTypeById(PET_TYPE_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(PetType.class, PET_TYPE_ID_1.toString())
        );
        assertThat(updatedPetType.getName()).isEqualTo(newName);
    }

    @Test
    @Transactional
    void shouldDeletePetType(){
        PetType petType = this.clinicService.findPetTypeById(PET_TYPE_ID_1).orElseThrow(
            () -> new ObjectRetrievalFailureException(PetType.class, PET_TYPE_ID_1.toString())
        );
        this.clinicService.deletePetType(petType);
        
        Optional<PetType> deletedPetType = this.clinicService.findPetTypeById(PET_TYPE_ID_1);
        assertThat(deletedPetType).isEmpty();
    }
}