package org.springframework.petmanagement.service.clinicService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.service.impl.ClinicServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTests {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void shouldFindAllClinics() {
        // Given
        Clinic clinic1 = Clinic.builder()
            .id(UUID.randomUUID())
            .name("クリニックA")
            .build();
        Clinic clinic2 = Clinic.builder()
            .id(UUID.randomUUID())
            .name("クリニックB")
            .build();

        when(clinicRepository.findAll()).thenReturn(Arrays.asList(clinic1, clinic2));

        // When
        List<Clinic> result = clinicService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(clinic1, clinic2);
        verify(clinicRepository, times(1)).findAll();
    }

    @Test
    void shouldFindClinicById() {
        // Given
        UUID clinicId = UUID.randomUUID();
        Clinic clinic = Clinic.builder()
            .id(clinicId)
            .name("テストクリニック")
            .build();

        when(clinicRepository.findById(clinicId)).thenReturn(clinic);

        // When
        Optional<Clinic> result = clinicService.findById(clinicId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("テストクリニック");
        verify(clinicRepository, times(1)).findById(clinicId);
    }

    @Test
    void shouldReturnEmptyWhenClinicNotFound() {
        // Given
        UUID clinicId = UUID.randomUUID();
        when(clinicRepository.findById(clinicId)).thenReturn(null);

        // When
        Optional<Clinic> result = clinicService.findById(clinicId);

        // Then
        assertThat(result).isEmpty();
        verify(clinicRepository, times(1)).findById(clinicId);
    }

    @Test
    void shouldSaveClinic() {
        // Given
        Clinic clinic = Clinic.builder()
            .name("新規クリニック")
            .telephone("03-1234-5678")
            .build();

        // When
        Clinic result = clinicService.save(clinic);

        // Then
        assertThat(result.getName()).isEqualTo("新規クリニック");
        verify(clinicRepository, times(1)).save(any(Clinic.class));
    }

    @Test
    void shouldDeleteClinic() {
        // Given
        UUID clinicId = UUID.randomUUID();
        Clinic clinic = Clinic.builder()
            .id(clinicId)
            .name("削除対象クリニック")
            .build();

        when(clinicRepository.findById(clinicId)).thenReturn(clinic);

        // When
        clinicService.deleteClinic(clinicId);

        // Then
        verify(clinicRepository, times(1)).findById(clinicId);
        verify(clinicRepository, times(1)).delete(clinic);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentClinic() {
        // Given
        UUID clinicId = UUID.randomUUID();
        when(clinicRepository.findById(clinicId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> clinicService.deleteClinic(clinicId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("clinic not found");
        
        verify(clinicRepository, times(1)).findById(clinicId);
        verify(clinicRepository, never()).delete(any(Clinic.class));
    }
}
