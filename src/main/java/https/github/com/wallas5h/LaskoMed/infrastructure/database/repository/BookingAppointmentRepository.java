package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.AvailableAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.MedicalAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.BookingAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.BookingAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookingAppointmentRepository implements BookingAppointmentDAO {

  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private BookingAppointmentJpaRepository bookingAppointmentJpaRepository;
  private MedicalAppointmentJpaRepository medicalAppointmentJpaRepository;
  private DiagnosesDiseaseJpaRepository diagnosesDiseaseJpaRepository;
  private EntityManager entityManager;

  private BookingAppointmentMapper bookingAppointmentMapper;
  private MedicalAppointmentMapper medicalAppointmentMapper;
  private AvailableAppointmentMapper availableAppointmentMapper;

  @Override
  public List<BookingAppointmentDTO> findByPatientId(Long patientId) {
    return bookingAppointmentJpaRepository.findByPatientId(patientId).stream()
        .map(a -> bookingAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  @Override
  public int updateBookingStatusByPatient(long bookingId, String bookingStatus, Long patientId, OffsetDateTime currentDateTime) {

    return bookingAppointmentJpaRepository.updateBookingStatusByPatient(
        bookingId,
        bookingStatus,
        patientId,
        currentDateTime
    );
  }

  @Override
  public Optional<BookingAppointmentEntity> findById(Long id) {
    return bookingAppointmentJpaRepository.findById(id);
  }

  @Override
  public List<BookingAppointmentDTO> findByDoctorId(Long doctorId) {
    return bookingAppointmentJpaRepository.findByDoctorId(doctorId).stream()
        .map(bookingAppointmentMapper::mapFromEntityToDto)
        .toList();
  }

  @Override
  public List<BookingAppointmentDTO> findByDoctorIdAndDateRange(Long doctorId, OffsetDateTime fromDateTime, OffsetDateTime toDateTime) {
    return bookingAppointmentJpaRepository.findByDoctorIdAndDateRange(doctorId, fromDateTime, toDateTime).stream()
        .map(bookingAppointmentMapper::mapFromEntityToDto)
        .toList();
  }

  @Override
  public void updateBookingStatus(Long bookingId, String appointmentStatus) {
    bookingAppointmentJpaRepository.updateBookingStatus(bookingId, appointmentStatus);
  }

  @Override
  public Optional<BookingAppointmentEntity> findByPatientIdAndDate(Long patientId, OffsetDateTime date) {
    return bookingAppointmentJpaRepository.findByPatientIdAndDate(patientId, date);
  }

  @Override
  public BookingAppointmentEntity save(BookingAppointmentEntity newReservedAppointment) {
    return bookingAppointmentJpaRepository.save(newReservedAppointment);
  }
}
