package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingAppointmentRepository extends JpaRepository<BookingAppointmentEntity, Long> {

  @Query("""
      SELECT ba FROM BookingAppointmentEntity ba
      WHERE ba.doctor.doctorId = :id
      AND ba.bookingStatus IN ('PENDING', 'CONFIRMED')
      ORDER BY CAST(ba.bookingDate AS date)
      """)
  List<BookingAppointmentEntity> findByDoctorId(final @Param("id") Long doctorId);
  @Query("""
      SELECT ba FROM BookingAppointmentEntity ba
      WHERE ba.doctor.doctorId = :id
      AND ba.bookingStatus IN ('PENDING', 'CONFIRMED')
      AND ba.bookingDate BETWEEN :startDate AND :endDate
      ORDER BY CAST(ba.bookingDate AS date)
      """)
  List<BookingAppointmentEntity> findByDoctorIdAndDateRange(
      @Param("id") Long doctorId,
      @Param("startDate") OffsetDateTime startDate,
      @Param("endDate") OffsetDateTime endDate
  );

  @Query("""
      SELECT ba FROM BookingAppointmentEntity ba
      WHERE ba.patient.patientId = :id
      AND ba.bookingStatus IN ('PENDING', 'CONFIRMED')
      ORDER BY CAST(ba.bookingDate AS date)
      """)
  List<BookingAppointmentEntity> findByPatientId(final @Param("id") Long patientId);

  @Modifying
  @Query("""
      UPDATE BookingAppointmentEntity ba
      SET ba.bookingStatus = :newStatus
      WHERE ba.bookingId = :bookingId
      """)
  int updateBookingStatus(@Param("bookingId") Long bookingId,
                          @Param("newStatus") String newStatus);
  @Modifying
  @Query("""
      UPDATE BookingAppointmentEntity ba
      SET ba.bookingStatus = :newStatus
      WHERE ba.bookingId = :bookingId
      AND ba.patient.patientId = :id
      AND ba.bookingDate>= :date
      """)
  int updateBookingStatusByPatient(
      @Param("bookingId") Long bookingId,
      @Param("newStatus") String newStatus,
      final @Param("id") Long patientId,
      @Param("date")OffsetDateTime date
      );

  @Query("""
      SELECT ba FROM BookingAppointmentEntity ba
      WHERE ba.patient.patientId = :id
      AND ba.bookingStatus IN ('PENDING', 'CONFIRMED')
      AND ba.bookingDate= :date
      """)
  Optional<BookingAppointmentEntity> findByPatientIdAndDate(
      final @Param("id") Long patientId,
      @Param("date")OffsetDateTime date
  );
}