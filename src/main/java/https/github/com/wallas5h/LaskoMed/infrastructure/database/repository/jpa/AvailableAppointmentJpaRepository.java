package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;


import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AvailableAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailableAppointmentJpaRepository extends JpaRepository<AvailableAppointmentEntity, Long> {

  @Query("""
    SELECT aa FROM AvailableAppointmentEntity aa
    WHERE aa.dateAvailable >= :date
    AND aa.isActive= true
    AND (COALESCE(:specialization, '') = '' OR aa.doctor.specialization = :specialization)
    AND (COALESCE(:location, '') = '' OR aa.clinic.address.city = :location)
    ORDER BY aa.dateAvailable, aa.startTime
    """)
  List<AvailableAppointmentEntity> getAvailableMedicalAppointments(
      @Param("date") LocalDate date,
      @Param("specialization") String specialization,
      @Param("location") String location
  );

}
