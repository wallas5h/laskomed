package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.api.dto.DiagnosedDiseaseDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DiagnosesDiseaseRepository extends JpaRepository<DiagnosedDiseaseEntity, Long> {

  @Query("""
      SELECT dd FROM DiagnosedDiseaseEntity dd
      WHERE dd.patient.patientId = :id
      ORDER BY dd.diseaseName, dd.diagnosisDate
      """)
  Set<DiagnosedDiseaseEntity> findByPatientId(
      final @Param("id") Long patientId
  );

}
