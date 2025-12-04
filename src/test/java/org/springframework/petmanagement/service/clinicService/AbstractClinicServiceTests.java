package org.springframework.petmanagement.service.clinicService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.*;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
abstract class AbstractClinicServiceTests {

    @Autowired
    protected ClinicService clinicService;

    @Autowired
    private EntityManager em;

    private PetType catType;
    private PetType dogType;

    @BeforeEach
    void setup() {
        long timestamp = System.currentTimeMillis();

        catType = new PetType();
        catType.setName("cat_" + timestamp);
        clinicService.savePetType(catType);

        dogType = new PetType();
        dogType.setName("dog_" + timestamp);
        clinicService.savePetType(dogType);
        
        em.flush();
    }

    @Test
    void shouldFindOwnersByKana() {
        Owner owner = createOwner("Taro", "Yamada", "タロウ", "ヤマダ");
        clinicService.saveOwner(owner);
        em.flush(); // DBへ反映

        // 修正: リポジトリが LIKE :param になったため、呼び出し側で % をつける
        Collection<Owner> owners = this.clinicService.findOwnerByKana("%ヤマダ%", null);
        assertThat(owners).hasSize(1);
    }

    @Test
    void shouldInsertOwner() {
        Owner owner = createOwner("Sam", "Schultz", "サム", "シュルツ");
        this.clinicService.saveOwner(owner);
        em.flush(); // DBへ反映
        
        assertThat(owner.getId()).isNotNull();
        
        // 修正: 呼び出し側で % をつける
        Collection<Owner> owners = this.clinicService.findOwnerByKana("%シュルツ%", null);
        assertThat(owners).isNotEmpty();
    }

    @Test
    void shouldUpdateOwner() {
        Owner owner = createOwner("Jiro", "Sato", "ジロウ", "サトウ");
        this.clinicService.saveOwner(owner);
        em.flush();

        String newLastName = "SatoUpdate";
        owner.setLastName(newLastName);
        this.clinicService.saveOwner(owner);
        em.flush();

        Owner updatedOwner = this.clinicService.findOwnerById(owner.getId()).orElseThrow();
        assertThat(updatedOwner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindSingleOwnerWithPet() {
        Owner owner = createOwner("Hanako", "Suzuki", "ハナコ", "スズキ");
        Pet pet = createPet(owner, catType, "Tama");
        owner.addPet(pet);
        clinicService.saveOwner(owner);
        clinicService.savePet(pet);
        em.flush();

        Owner foundOwner = this.clinicService.findOwnerById(owner.getId()).orElseThrow();
        assertThat(foundOwner.getLastName()).isEqualTo("Suzuki");
        assertThat(foundOwner.getPets()).hasSize(1);
        assertThat(foundOwner.getPets().iterator().next().getName()).isEqualTo("Tama");
    }

    @Test
    void shouldFindPetWithCorrectId() {
        Owner owner = createOwner("Jean", "Doe", "ジーン", "ドゥ");
        Pet pet = createPet(owner, dogType, "Samantha");
        clinicService.saveOwner(owner);
        clinicService.savePet(pet);
        em.flush();

        Pet foundPet = this.clinicService.findPetById(pet.getId()).orElseThrow();
        assertThat(foundPet.getName()).isEqualTo("Samantha");
        assertThat(foundPet.getOwner().getFirstName()).isEqualTo("Jean");
    }

    @Test
    void shouldInsertPet() {
        Owner owner = createOwner("Tom", "Cruise", "トム", "クルーズ");
        this.clinicService.saveOwner(owner);

        Pet pet = new Pet();
        pet.setName("bowser");
        pet.setSex("male");
        pet.setBirthDate(LocalDate.now());
        pet.setType(dogType);
        
        owner.addPet(pet);
        this.clinicService.savePet(pet);
        em.flush();

        assertThat(pet.getId()).isNotNull();
        assertThat(owner.getPets()).hasSize(1);
    }

    @Test
    void shouldUpdatePetName() {
        Owner owner = createOwner("Mike", "Tyson", "マイク", "タイソン");
        Pet pet = createPet(owner, catType, "Leo");
        clinicService.saveOwner(owner);
        clinicService.savePet(pet);
        em.flush();

        String newName = "LeoUpdated";
        pet.setName(newName);
        this.clinicService.savePet(pet);
        em.flush();

        Pet updatedPet = this.clinicService.findPetById(pet.getId()).orElseThrow();
        assertThat(updatedPet.getName()).isEqualTo(newName);
    }

    @Test
    void shouldDeletePet() {
        Owner owner = createOwner("Bob", "Marley", "ボブ", "マーリー");
        Pet pet = createPet(owner, dogType, "Rex");
        clinicService.saveOwner(owner);
        clinicService.savePet(pet);
        em.flush();
        
        UUID petId = pet.getId();

        this.clinicService.deletePet(pet);
        em.flush();

        assertThat(this.clinicService.findPetById(petId)).isEmpty();
    }

    @Test
    void shouldDeleteOwner() {
        Owner owner = createOwner("Alice", "Wonder", "アリス", "ワンダー");
        this.clinicService.saveOwner(owner);
        em.flush();
        UUID ownerId = owner.getId();

        this.clinicService.deleteOwner(owner);
        em.flush();

        assertThat(this.clinicService.findOwnerById(ownerId)).isEmpty();
    }

    @Test
    void shouldInsertPetType() {
        PetType petType = new PetType();
        petType.setName("tiger_" + System.currentTimeMillis()); 
        this.clinicService.savePetType(petType);
        em.flush();

        assertThat(petType.getId()).isNotNull();
        assertThat(this.clinicService.findPetTypeById(petType.getId())).isPresent();
    }

    @Test
    void shouldUpdatePetType() {
        PetType petType = new PetType();
        petType.setName("hamster_" + System.currentTimeMillis());
        this.clinicService.savePetType(petType);
        em.flush();

        petType.setName("hamsterUpdated_" + System.currentTimeMillis());
        this.clinicService.savePetType(petType);
        em.flush();

        PetType updated = this.clinicService.findPetTypeById(petType.getId()).orElseThrow();
        assertThat(updated.getName()).startsWith("hamsterUpdated");
    }

    @Test
    void shouldDeletePetType() {
        PetType petType = new PetType();
        petType.setName("snake_" + System.currentTimeMillis());
        this.clinicService.savePetType(petType);
        em.flush();
        UUID typeId = petType.getId();

        this.clinicService.deletePetType(petType);
        em.flush();

        assertThat(this.clinicService.findPetTypeById(typeId)).isEmpty();
    }

    // --- Helper Methods ---

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