package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.business.dao.DiagnosedDiseaseDAO;
import https.github.com.wallas5h.LaskoMed.business.services.AppointmentsService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
@AllArgsConstructor
public class DiagnosedDiseaseRepository implements DiagnosedDiseaseDAO {
  private DiagnosesDiseaseJpaRepository diagnosesDiseaseJpaRepository;

  @Override
  public void createDiagnosedDiseaseAndSave(
      PatientEntity patient, DoctorEntity doctor, String diagnosis,
      String doctorComment, OffsetDateTime date) {
    DiagnosedDiseaseEntity newDisease = DiagnosedDiseaseEntity.builder()
        .patient(patient)
        .doctor(doctor)
        .diseaseName(diagnosis)
        .description(doctorComment)
        .diagnosisDate(date)
        .build();
    diagnosesDiseaseJpaRepository.save(newDisease);
  }
}
