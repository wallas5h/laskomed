package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiagnosesDiseaseJpaRepository extends JpaRepository<DiagnosedDiseaseEntity, Long> {

  @Query("""
      SELECT dd FROM DiagnosedDiseaseEntity dd
      WHERE dd.patient.patientId = :id
      ORDER BY dd.diseaseName, dd.diagnosisDate
      """)
  Set<DiagnosedDiseaseEntity> findByPatientId(
      final @Param("id") Long patientId
  );

}
