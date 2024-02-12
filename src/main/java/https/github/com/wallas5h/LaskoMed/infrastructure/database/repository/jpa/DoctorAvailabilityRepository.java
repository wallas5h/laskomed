package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, Long> {

  @Query("""
        SELECT dae FROM DoctorAvailabilityEntity dae
        WHERE dae.doctor.doctorId = :id
        ORDER BY dae.dateAvailable, dae.startTime
        """)
  List<DoctorAvailabilityEntity> findByDoctorId(final @Param("id") Long doctorId);

  @Query("""
      SELECT da FROM DoctorAvailabilityEntity da
      WHERE da.doctor.doctorId = :doctorId
      AND :currentDate <= da.dateAvailable
      ORDER BY da.dateAvailable, da.startTime
      """)
  List<DoctorAvailabilityEntity> findPresentAvailabilities(
      @Param("doctorId") Long doctorId,
      @Param("currentDate") LocalDate currentDate);

  @Query("""
      SELECT da FROM DoctorAvailabilityEntity da
      WHERE da.doctor.doctorId = :doctorId
      AND :dateAvailable BETWEEN da.dateAvailable AND da.dateAvailable
      AND :startTime < da.endTime
      AND :endTime > da.startTime
      """)
  List<DoctorAvailabilityEntity> findConflictingAvailabilities(
      @Param("doctorId") Long doctorId,
      @Param("dateAvailable") LocalDate dateAvailable,
      @Param("startTime") LocalTime startTime,
      @Param("endTime") LocalTime endTime);
}
