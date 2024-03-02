package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.controller.GlobalExceptionHandler;
import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.business.dao.AvailableAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.BookingAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.DiagnosedDiseaseDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.MedicalAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentsService {
  private final BookingAppointmentDAO bookingAppointmentDAO;
  private final MedicalAppointmentDAO medicalAppointmentDAO;
  private final AvailableAppointmentDAO availableAppointmentDAO;
  private final DiagnosedDiseaseDAO diagnosedDiseaseDAO;

  private EntityManager entityManager;

  private static OffsetDateTime getOffsetDateTime(AvailableAppointmentEntity availableAppointment) {
    return OffsetDateTime.of(
        availableAppointment.getDateAvailable(),
        availableAppointment.getStartTime(),
        ZoneOffset.UTC);
  }

  public List<BookingAppointmentDTO> getDoctorAllAppointments(Long doctorId) {
    return bookingAppointmentDAO.findByDoctorId(doctorId);
  }

  public List<BookingAppointmentDTO> getDoctorAppointmentsByDateRange(
      Long doctorId,
      OffsetDateTime fromDateTime,
      OffsetDateTime toDateTime) {
    return bookingAppointmentDAO.findByDoctorIdAndDateRange(doctorId, fromDateTime, toDateTime);

  }

  public List<BookingAppointmentDTO> getPatientUpcomingBookings(Long patientId) {
    return bookingAppointmentDAO.findByPatientId(patientId);
  }

  @Transactional
  public void createAppointmentsFromDoctorAvailabilities(DoctorService.CreatedAppointmentDTO2 request) {
    LocalDate dateAvailable = request.dateAvailable();
    LocalTime startTime = request.startTime();
    LocalTime endTime = request.endTime();

    DoctorEntity doctor = entityManager.getReference(
        DoctorEntity.class, request.doctorId());
    ClinicEntity clinic = entityManager.getReference(
        ClinicEntity.class, request.clincId());

    Duration appointmentDuration = Duration.ofMinutes(20);
    LocalTime currentTime = startTime;

    while (currentTime.plus(appointmentDuration).isBefore(endTime) || currentTime.plus(appointmentDuration).equals(endTime)) {

      AvailableAppointmentEntity newCreatedAppointment = AvailableAppointmentEntity.builder()
          .doctor(doctor)
          .clinic(clinic)
          .dateAvailable(dateAvailable)
          .startTime(currentTime)
          .endTime(currentTime.plus(appointmentDuration))
          .isActive(true)
          .build();

      availableAppointmentDAO.save(newCreatedAppointment);

      currentTime = currentTime.plus(appointmentDuration);
    }
  }

  @Transactional
  public MedicalAppointmentEntity addMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);
    if (request.isAddNewDisease()) {
      diagnosedDiseaseDAO.createDiagnosedDiseaseAndSave(
          result.newMedicalAppointmentEntity.getPatient(),
          result.newMedicalAppointmentEntity.getDoctor(),
          result.newMedicalAppointmentEntity.getDiagnosis(),
          result.newMedicalAppointmentEntity.getDoctorComment(),
          OffsetDateTime.now()
      );

    }
    bookingAppointmentDAO.updateBookingStatus(result.bookingAppointment().getBookingId(),
        result.newMedicalAppointmentEntity().getAppointmentStatus().toUpperCase());

    return medicalAppointmentDAO.save(result.newMedicalAppointmentEntity());
  }

  @Transactional
  public int updateMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);

    int rowUpdated = medicalAppointmentDAO
        .updateByBookingId(result.bookingAppointment().getBookingId(), result.newMedicalAppointmentEntity());
    bookingAppointmentDAO.updateBookingStatus(result.bookingAppointment().getBookingId(),
        result.newMedicalAppointmentEntity().getAppointmentStatus());
    return rowUpdated;
  }

  private MapResult mapRequestDtoToEntity(MedicalAppointmentRequestDTO request) {
    PatientEntity patient = entityManager.find(
        PatientEntity.class, request.getPatientId());
    DoctorEntity doctor = entityManager.find(
        DoctorEntity.class, request.getDoctorId());
    ClinicEntity clinic = entityManager.find(
        ClinicEntity.class, request.getClinicId());
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());


    MedicalAppointmentEntity newMedicalAppointmentEntity = MedicalAppointmentEntity.builder()
        .patient(patient)
        .doctor(doctor)
        .clinic(clinic)
        .diagnosis(request.getDiagnosis())
        .appointmentStatus(request.getAppointmentStatus())
        .prescription(request.getPrescription())
        .bookingAppointment(bookingAppointment)
        .doctorComment(request.getDoctorComment())
        .cost(getMedicalAppointmentCost(patient.getMedicalPackage(), bookingAppointment.getType(), request.getAppointmentStatus()))
        .build();
    MapResult result = new MapResult(bookingAppointment, newMedicalAppointmentEntity);
    return result;
  }

  @Transactional
  public Boolean isExistMedicalAppointment(MedicalAppointmentRequestDTO request) {
    try{
      getMedicalAppointmentById(request);
      return true;
    } catch (Exception e){
      return false;
    }
  }

  public MedicalAppointmentDTO getMedicalAppointmentById(MedicalAppointmentRequestDTO request) {
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());

    return medicalAppointmentDAO.findByBookingId(bookingAppointment.getBookingId());
  }

  private BigDecimal getMedicalAppointmentCost(String medicalPackage, String type, String status) {

    if (medicalPackage.equalsIgnoreCase(EnumsContainer.MedicalPackage.PREMIUM.name())) {
      return BigDecimal.ZERO;
    }

    if (status.equalsIgnoreCase(EnumsContainer.AppointmentStatus.MISSED.name())) {
      return BigDecimal.valueOf(50);
    }
    try {
      EnumsContainer.AppointmentType appointmentType = EnumsContainer.AppointmentType.valueOf(type.toUpperCase());

      switch (appointmentType) {
        case ONLINE:
          return BigDecimal.valueOf(100);
        case TELECONSULTATION:
          return BigDecimal.valueOf(80);
        case FACILITY:
          return BigDecimal.valueOf(150);
        default:
          return BigDecimal.valueOf(100);
      }
    } catch (IllegalArgumentException e) {
      System.err.println("Nieprawidłowy typ wizyty: " + type);
      return BigDecimal.valueOf(100);
    }
  }

  @Transactional
  public int changeBookingStatus(Long patientId, BookingAppointmentRequestDTO request) {
    isBookingStatusValid(request);
    OffsetDateTime currentDateTime= OffsetDateTime.now();

    changeAvailableAppointmentToActiveIfRequestIsToCanceled(request);

    return bookingAppointmentDAO.updateBookingStatusByPatient(
        Long.parseLong(request.getBookingId()),
        request.getBookingStatus(),
        patientId,
        currentDateTime
    );

  }

  private void changeAvailableAppointmentToActiveIfRequestIsToCanceled(BookingAppointmentRequestDTO request) {
    if(request.getBookingStatus().equals(EnumsContainer.BookingStatus.CANCELLED.name())){
      Optional<BookingAppointmentEntity> bookingEntity = bookingAppointmentDAO.findById(Long.valueOf(request.getBookingId()));
      if(bookingEntity.isPresent()){
        availableAppointmentDAO.changeAvailableAppointmentToActive(bookingEntity);
      }
    }
  }

  public List<MedicalAppointmentDTO> getPatientMedicalAppointments(Long patientId) {
    return medicalAppointmentDAO.findByPatientId(patientId);

  }
  public List<MedicalAppointmentDTO> getPatientMedicalAppointments(Long patientId, String specialization) {
    return medicalAppointmentDAO.findByPatientIdAndSpecialization(patientId, specialization);
  }

  public MedicalAppointmentDTO getPatientMedicalAppointmentDeatails(Long appointmentId) {
    return medicalAppointmentDAO.findById(appointmentId);
  }

  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(LocalDate date, String specialization, String location) {
    return availableAppointmentDAO.getAvailableMedicalAppointments(date, specialization, location);
  }

  public Optional<BookingAppointmentEntity> findAppointmentByDateAndPatient(AppointmentReservationRequestDto request) {
    AvailableAppointmentEntity availableAppointment = entityManager.find(
        AvailableAppointmentEntity.class, request.getAvailableAppointmentId());
    PatientEntity patient = entityManager.find(
        PatientEntity.class, request.getPatientId());

    OffsetDateTime date = getOffsetDateTime(availableAppointment);
    return bookingAppointmentDAO.findByPatientIdAndDate(patient.getPatientId(), date);


  }

  public BookingAppointmentEntity reserveAppointment(AppointmentReservationRequestDto request) {

    AvailableAppointmentEntity availableAppointment = entityManager.find(
        AvailableAppointmentEntity.class, request.getAvailableAppointmentId());
    PatientEntity patient = entityManager.find(
        PatientEntity.class, request.getPatientId());

    if (!availableAppointment.getIsActive()) {
      return null;
    }

    OffsetDateTime bookingDate = getOffsetDateTime(availableAppointment);
    String type = isValidAppointmentType(request) ? request.getAppointmentType().toUpperCase() :
        EnumsContainer.AppointmentType.FACILITY.name();

    BookingAppointmentEntity newReservedAppointment = BookingAppointmentEntity.builder()
        .bookingDate(bookingDate)
        .doctor(availableAppointment.getDoctor())
        .clinic(availableAppointment.getClinic())
        .availableAppointment(availableAppointment)
        .type(type)
        .patient(patient)
        .bookingStatus(EnumsContainer.BookingStatus.PENDING.name())
        .build();

    BookingAppointmentEntity bookingAppointmentEntity = bookingAppointmentDAO.save(newReservedAppointment);
    availableAppointment.setIsActive(false);
    availableAppointmentDAO.save(availableAppointment);
    return bookingAppointmentEntity;
  }

  public boolean isBookingStatusValid(BookingAppointmentRequestDTO request) {
    try {
      EnumsContainer.BookingStatus status = EnumsContainer.BookingStatus.valueOf(request.getBookingStatus());
      return true;
    } catch (IllegalArgumentException e) {
      new GlobalExceptionHandler().handle(e);
      return false;
    }
  }

  public boolean isValidAppointmentType(AppointmentReservationRequestDto request) {
    try {
      EnumsContainer.AppointmentType.valueOf(request.getAppointmentType().toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public record MapResult(BookingAppointmentEntity bookingAppointment,
                           MedicalAppointmentEntity newMedicalAppointmentEntity) {
  }
}
