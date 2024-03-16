package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorAvailabilityMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorAvailabilityDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorAvailabilityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Repository
public class DoctorAvailabilityRepository implements DoctorAvailabilityDAO {
  private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
  private DoctorAvailabilityMapper doctorAvailabilityMapper;

  @Override
  public List<DoctorAvailabilityDTO> findByDoctorId(Long doctorId) {
    return doctorAvailabilityJpaRepository.findByDoctorId(doctorId).stream()
        .map(a -> doctorAvailabilityMapper.mapFromEntityToDto(a))
        .toList();
  }

  @Override
  public List<DoctorAvailabilityDTO> findPresentAvailabilities(Long doctorId, LocalDate now) {
    return doctorAvailabilityJpaRepository.findPresentAvailabilities(doctorId, LocalDate.now())
        .stream()
        .map(a -> doctorAvailabilityMapper.mapFromEntityToDto(a))
        .toList();
  }

  @Override
  public DoctorAvailabilityEntity save(DoctorAvailabilityEntity newAvailability) {
    return doctorAvailabilityJpaRepository.save(newAvailability);
  }

  @Override
  public List<DoctorAvailabilityEntity> findConflictingAvailabilities(
      Long id, LocalDate dateAvailable, LocalTime startTime, LocalTime endTime
  ) {
    return doctorAvailabilityJpaRepository.findConflictingAvailabilities(id, dateAvailable, startTime, endTime);

  }
}
