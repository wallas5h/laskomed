package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorAvailabilityDAO {
  List<DoctorAvailabilityDTO> findByDoctorId(Long doctorId);

  List<DoctorAvailabilityDTO> findPresentAvailabilities(Long doctorId, LocalDate now);

  DoctorAvailabilityEntity save(DoctorAvailabilityEntity newAvailability);

  List<DoctorAvailabilityEntity> findConflictingAvailabilities(Long id, LocalDate dateAvailable, LocalTime startTime, LocalTime endTime);
}
