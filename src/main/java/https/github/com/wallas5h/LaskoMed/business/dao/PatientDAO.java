package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;

import java.util.Optional;

public interface PatientDAO {
  PatientDTO findById(Long patientId);

  PatientEntity save(PatientEntity newPatientEntiy);

  PatientDTO findByUserId(Long userId);

  Optional<PatientEntity>  findByUserIdOptional(Long userId);
}
