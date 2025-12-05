package org.springframework.petmanagement.service.managementService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.*;
import org.springframework.petmanagement.service.ManagementService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
abstract class AbstractManagementServiceTests {

    @Autowired
    protected ManagementService managementService;

    @Autowired
    private EntityManager em;

    private PetType catType;
    private PetType dogType;

    @BeforeEach
    void setup() {
        long timestamp = System.currentTimeMillis();

        catType = new PetType();
        catType.setName("cat_" + timestamp);
        managementService.savePetType(catType);

        dogType = new PetType();
        dogType.setName("dog_" + timestamp);
        managementService.savePetType(dogType);
        
        em.flush();
    }

    @Test
    void shouldFindOwnersByKana() {

        Owner owner = createOwner("Taro", "Yamada", "タロウ", "テストヤマダ");
        
        managementService.saveOwner(owner);
        em.flush();

        Collection<Owner> owners = this.managementService.findOwnerByKana("%テストヤマダ%", null);
        
        assertThat(owners).hasSize(1);
    }

    @Test
    void shouldInsertOwner() {
        Owner owner = createOwner("Sam", "Schultz", "サム", "シュルツ");
        this.managementService.saveOwner(owner);
        em.flush();
        
        assertThat(owner.getId()).isNotNull();
        
        Collection<Owner> owners = this.managementService.findOwnerByKana("%シュルツ%", null);
        assertThat(owners).isNotEmpty();
    }

    @Test
    void shouldUpdateOwner() {
        Owner owner = createOwner("Jiro", "Sato", "ジロウ", "サトウ");
        this.managementService.saveOwner(owner);
        em.flush();

        String newLastName = "SatoUpdate";
        owner.setLastName(newLastName);
        this.managementService.saveOwner(owner);
        em.flush();

        Owner updatedOwner = this.managementService.findOwnerById(owner.getId()).orElseThrow();
        assertThat(updatedOwner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = createOwner("Hanako", "Suzuki", "ハナコ", "スズキ");
        Pet pet = createPet(owner, catType, "Tama");
        owner.addPet(pet);
        managementService.saveOwner(owner);
        managementService.savePet(pet);
        em.flush();

        Owner foundOwner = this.managementService.findOwnerById(owner.getId()).orElseThrow();
        assertThat(foundOwner.getLastName()).isEqualTo("Suzuki");
        assertThat(foundOwner.getPets()).hasSize(1);
        assertThat(foundOwner.getPets().iterator().next().getName()).isEqualTo("Tama");
    }

    @Test
    void shouldFindPetWithCorrectId() {
        Owner owner = createOwner("Jean", "Doe", "ジーン", "ドゥ");
        Pet pet = createPet(owner, dogType, "Samantha");
        managementService.saveOwner(owner);
        managementService.savePet(pet);
        em.flush();

        Pet foundPet = this.managementService.findPetById(pet.getId()).orElseThrow();
        assertThat(foundPet.getName()).isEqualTo("Samantha");
        assertThat(foundPet.getOwner().getFirstName()).isEqualTo("Jean");
    }

    @Test
    void shouldInsertPet() {
        Owner owner = createOwner("Tom", "Cruise", "トム", "クルーズ");
        this.managementService.saveOwner(owner);

        Pet pet = new Pet();
        pet.setName("bowser");
        pet.setSex("male");
        pet.setBirthDate(LocalDate.now());
        pet.setType(dogType);
        
        owner.addPet(pet);
        this.managementService.savePet(pet);
        em.flush();

        assertThat(pet.getId()).isNotNull();
        assertThat(owner.getPets()).hasSize(1);
    }

    @Test
    void shouldUpdatePetName() {
        Owner owner = createOwner("Mike", "Tyson", "マイク", "タイソン");
        Pet pet = createPet(owner, catType, "Leo");
        managementService.saveOwner(owner);
        managementService.savePet(pet);
        em.flush();

        String newName = "LeoUpdated";
        pet.setName(newName);
        this.managementService.savePet(pet);
        em.flush();

        Pet updatedPet = this.managementService.findPetById(pet.getId()).orElseThrow();
        assertThat(updatedPet.getName()).isEqualTo(newName);
    }

    @Test
    void shouldDeletePet() {
        Owner owner = createOwner("Bob", "Marley", "ボブ", "マーリー");
        Pet pet = createPet(owner, dogType, "Rex");
        managementService.saveOwner(owner);
        managementService.savePet(pet);
        em.flush();
        
        UUID petId = pet.getId();

        this.managementService.deletePet(pet);
        em.flush();

        assertThat(this.managementService.findPetById(petId)).isEmpty();
    }

    @Test
    void shouldDeleteOwner() {
        Owner owner = createOwner("Alice", "Wonder", "アリス", "ワンダー");
        this.managementService.saveOwner(owner);
        em.flush();
        UUID ownerId = owner.getId();

        this.managementService.deleteOwner(owner);
        em.flush();

        assertThat(this.managementService.findOwnerById(ownerId)).isEmpty();
    }

    @Test
    void shouldInsertPetType() {
        PetType petType = new PetType();
        petType.setName("tiger_" + System.currentTimeMillis()); 
        this.managementService.savePetType(petType);
        em.flush();

        assertThat(petType.getId()).isNotNull();
        assertThat(this.managementService.findPetTypeById(petType.getId())).isPresent();
    }

    @Test
    void shouldUpdatePetType() {
        PetType petType = new PetType();
        petType.setName("hamster_" + System.currentTimeMillis());
        this.managementService.savePetType(petType);
        em.flush();

        petType.setName("hamsterUpdated_" + System.currentTimeMillis());
        this.managementService.savePetType(petType);
        em.flush();

        PetType updated = this.managementService.findPetTypeById(petType.getId()).orElseThrow();
        assertThat(updated.getName()).startsWith("hamsterUpdated");
    }

    @Test
    void shouldDeletePetType() {
        PetType petType = new PetType();
        petType.setName("snake_" + System.currentTimeMillis());
        this.managementService.savePetType(petType);
        em.flush();
        UUID typeId = petType.getId();

        this.managementService.deletePetType(petType);
        em.flush();

        assertThat(this.managementService.findPetTypeById(typeId)).isEmpty();
    }


    private Owner createOwner(String first, String last, String firstKana, String lastKana) {
        Owner owner = new Owner();
        owner.setFirstName(first);
        owner.setLastName(last);
        owner.setFirstNameKana(firstKana);
        owner.setLastNameKana(lastKana);
        owner.setAddress("123 Test St");
        owner.setCity("Test City");
        owner.setTelephone("1234567890");
        owner.setEmail(first + "." + last + "@example.com");
        return owner;
    }

    private Pet createPet(Owner owner, PetType type, String name) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setBirthDate(LocalDate.now());
        pet.setType(type);
        pet.setSex("male");
        pet.setOwner(owner);
        return pet;
    }
}