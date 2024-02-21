package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;

import java.time.OffsetDateTime;

public interface DiagnosedDiseaseDAO {

  void createDiagnosedDiseaseAndSave(PatientEntity patient, DoctorEntity doctor, String diagnosis, String doctorComment, OffsetDateTime now);
}
