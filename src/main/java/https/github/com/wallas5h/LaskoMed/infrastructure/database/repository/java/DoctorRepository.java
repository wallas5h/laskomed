package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.java;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDAO {
  private DoctorJpaRepository doctorJpaRepository;
  private DoctorMapper doctorMapper;
  @Override
  public DoctorDTO findAllByDoctorId(Long doctorId) {
    return doctorJpaRepository.findAllByDoctorId(doctorId)
        .map(a -> doctorMapper.mapFromEntityToDto(a))
        .orElseThrow(() -> new EntityNotFoundException(
            "Doctor details not found, doctorId: [%s]".formatted(doctorId)
        ));
  }
}
