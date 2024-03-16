package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.DiagnosedDiseaseMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.PatientDAO;
import https.github.com.wallas5h.LaskoMed.business.services.AppointmentsService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDAO {

  private PatientJpaRepository patientJpaRepository;
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private DiagnosesDiseaseJpaRepository diseaseRepository;

  private AppointmentsService appointmentsService;
  private PatientMapper patientMapper;
  private DiagnosedDiseaseMapper diagnosedDiseaseMapper;

  @Override
  public PatientDTO findById(Long patientId) {
    Optional<PatientEntity> optionalPatientEntity = patientJpaRepository.findById(patientId);
    return optionalPatientEntity.map(patientMapper::mapFromEntityToDto)
        .orElseThrow(() -> new EntityNotFoundException(
            "PatientEntity not found, patientId: [%s]".formatted(patientId)
        ));
  }

  @Override
  public PatientEntity save(PatientEntity newPatientEntiy) {
    return patientJpaRepository.save(newPatientEntiy);
  }

  @Override
  public PatientDTO findByUserId(Long userId) {
    Optional<PatientEntity> optionalPatientEntity = patientJpaRepository.findByUserId(userId);
    return optionalPatientEntity.map(patientMapper::mapFromEntityToDto)
        .orElseThrow(() -> new EntityNotFoundException(
            "PatientEntity not found, userId: [%s]".formatted(userId)
        ));
  }

  public Optional<PatientEntity> findByUserIdOptional(Long userId) {
    Optional<Optional<PatientEntity>> result = Optional.ofNullable(patientJpaRepository.findByUserId(userId));

    return result.flatMap(innerOptional -> innerOptional);
  }
}
