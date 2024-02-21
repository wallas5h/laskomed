package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;

public interface PatientDAO {
  PatientDTO findById(Long patientId);
}
