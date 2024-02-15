package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointmentEntity, Long> {

  @Query("""
      SELECT ma FROM MedicalAppointmentEntity ma
      WHERE ma.bookingAppointment.bookingId = :bookingId
      """)
  Optional<MedicalAppointmentEntity> findByBookingId(@Param("bookingId") Long bookingId);

  @Modifying
  @Query("UPDATE MedicalAppointmentEntity ma SET ma = :newData WHERE ma.bookingAppointment.bookingId = :bookingId")
  int updateByBookingId(@Param("bookingId") Long bookingId, @Param("newData") MedicalAppointmentEntity newData);
}

