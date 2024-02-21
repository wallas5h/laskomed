package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;

public interface DoctorDAO {
  DoctorDTO findAllByDoctorId(Long doctorId);
}
