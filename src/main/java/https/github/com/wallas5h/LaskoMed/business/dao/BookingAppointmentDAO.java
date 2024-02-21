package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingAppointmentDAO {
  List<BookingAppointmentDTO> findByPatientId(Long patientId);

  int updateBookingStatusByPatient(long bookingId, String bookingStatus, Long patientId, OffsetDateTime currentDateTime);

  Optional<BookingAppointmentEntity> findById(Long id);

  List<BookingAppointmentDTO> findByDoctorId(Long doctorId);

  List<BookingAppointmentDTO> findByDoctorIdAndDateRange(
      Long doctorId, OffsetDateTime fromDateTime, OffsetDateTime toDateTime);

  void updateBookingStatus(Long bookingId, String appointmentStatus);

  Optional<BookingAppointmentEntity> findByPatientIdAndDate(Long patientId, OffsetDateTime date);

  BookingAppointmentEntity save(BookingAppointmentEntity newReservedAppointment);
}
