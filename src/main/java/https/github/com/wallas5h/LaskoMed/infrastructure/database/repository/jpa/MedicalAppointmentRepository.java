package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

  @Query("""
      SELECT ma FROM MedicalAppointmentEntity ma
      WHERE ma.patient.patientId = :patientId
      ORDER BY ma.bookingAppointment.bookingDate
      """)
  List<MedicalAppointmentEntity> findByPatientId(@Param("patientId") Long patientId);

  @Query("""
      SELECT ma FROM MedicalAppointmentEntity ma
      WHERE ma.patient.patientId = :patientId
      AND (COALESCE(:specialization, '') = '' OR ma.doctor.specialization = :specialization)
      ORDER BY ma.bookingAppointment.bookingDate
      """)
  Optional<MedicalAppointmentEntity> findByPatientIdAndSpecialization(
      @Param("patientId") Long patientId,
      @Param("specialization") String specialization
  );
}
