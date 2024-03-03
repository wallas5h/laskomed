package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;

import java.util.Optional;

public interface DoctorDAO {
  DoctorDTO findAllByDoctorId(Long doctorId);

  Optional<DoctorEntity> findByUserIdOptional(Long userId);

  void save(DoctorEntity newDoctorEntity);

  DoctorDTO findByUserId(Long userId);
}
