package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.controller.GlobalExceptionHandler;
import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.AvailableAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.MedicalAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.BookingAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.MedicalAppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentsService {
  private AvailableAppointmentRepository availableAppointmentRepository;
  private BookingAppointmentRepository bookingAppointmentRepository;
  private MedicalAppointmentRepository medicalAppointmentRepository;
  private DiagnosesDiseaseRepository diagnosesDiseaseRepository;
  private EntityManager entityManager;

  private BookingAppointmentMapper bookingAppointmentMapper;
  private MedicalAppointmentMapper medicalAppointmentMapper;
  private AvailableAppointmentMapper availableAppointmentMapper;

  private static OffsetDateTime getOffsetDateTime(AvailableAppointmentEntity availableAppointment) {
    return OffsetDateTime.of(
        availableAppointment.getDateAvailable(),
        availableAppointment.getStartTime(),
        ZoneOffset.UTC);
  }

  public List<BookingAppointmentDTO> getDoctorAllAppointments(Long doctorId) {
    return bookingAppointmentRepository.findByDoctorId(doctorId).stream()
        .map(a -> bookingAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  public List<BookingAppointmentDTO> getDoctorAppointmentsByDateRange(
      Long doctorId,
      OffsetDateTime fromDateTime,
      OffsetDateTime toDateTime) {
    return bookingAppointmentRepository.findByDoctorIdAndDateRange(doctorId, fromDateTime, toDateTime).stream()
        .map(a -> bookingAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  public List<BookingAppointmentDTO> getPatientUpcomingBookings(Long patientId) {
    return bookingAppointmentRepository.findByPatientId(patientId).stream()
        .map(a -> bookingAppointmentMapper.mapFromEntityToDto(a))
        .toList();
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

      availableAppointmentRepository.save(newCreatedAppointment);

      currentTime = currentTime.plus(appointmentDuration);
    }
  }

  @Transactional
  public MedicalAppointmentEntity addMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);
    if (request.isAddNewDisease()) {
      DiagnosedDiseaseEntity newDisease = DiagnosedDiseaseEntity.builder()
          .patient(result.newMedicalAppointmentEntity.getPatient())
          .doctor(result.newMedicalAppointmentEntity.getDoctor())
          .diseaseName(result.newMedicalAppointmentEntity.getDiagnosis())
          .description(result.newMedicalAppointmentEntity.getDoctorComment())
          .diagnosisDate(OffsetDateTime.now())
          .build();
      diagnosesDiseaseRepository.save(newDisease);
    }
    bookingAppointmentRepository.updateBookingStatus(result.bookingAppointment().getBookingId(),
        result.newMedicalAppointmentEntity().getAppointmentStatus());
    return medicalAppointmentRepository.save(result.newMedicalAppointmentEntity());
  }

  @Transactional
  public int updateMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);

    int rowUpdated = medicalAppointmentRepository
        .updateByBookingId(result.bookingAppointment().getBookingId(), result.newMedicalAppointmentEntity());
    bookingAppointmentRepository.updateBookingStatus(result.bookingAppointment().getBookingId(),
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
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());

    Optional<MedicalAppointmentEntity> medicalAppointmentByBookingId = medicalAppointmentRepository
        .findByBookingId(bookingAppointment.getBookingId());
    return medicalAppointmentByBookingId.isPresent();
  }

  public MedicalAppointmentEntity getMedicalAppointmentById(MedicalAppointmentRequestDTO request) {
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());

    Optional<MedicalAppointmentEntity> medicalAppointmentByBookingId = medicalAppointmentRepository
        .findByBookingId(bookingAppointment.getBookingId());
    return medicalAppointmentByBookingId.get();
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

    int updatedBookingRows = bookingAppointmentRepository.updateBookingStatusByPatient(
        Long.parseLong(request.getBookingId()),
        request.getBookingStatus(),
        patientId,
        currentDateTime
    );

    return updatedBookingRows;
  }

// @TODO nie dziła
  private void changeAvailableAppointmentToActiveIfRequestIsToCanceled(BookingAppointmentRequestDTO request) {
    if(request.getBookingStatus().equals(EnumsContainer.BookingStatus.CANCELLED.name())){
      Optional<BookingAppointmentEntity> bookingEntity = bookingAppointmentRepository.findById(Long.valueOf(request.getBookingId()));
      if(bookingEntity.isPresent()){
        AvailableAppointmentEntity availableAppointment = bookingEntity.get().getAvailableAppointment();
        availableAppointment.setIsActive(true);
        availableAppointmentRepository.save(availableAppointment);
      }
    }
  }

  public List<MedicalAppointmentDTO> getPatientMedicalAppointments(Long patientId) {
    return medicalAppointmentRepository.findByPatientId(patientId).stream()
        .map(a -> medicalAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }
  public List<MedicalAppointmentDTO> getPatientMedicalAppointments(Long patientId, String specialization) {
    return medicalAppointmentRepository.findByPatientIdAndSpecialization(patientId, specialization).stream()
        .map(a -> medicalAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  public MedicalAppointmentDTO getPatientMedicalAppointmentDeatails(Long appointmentId) {
    return medicalAppointmentRepository.findById(appointmentId)
        .map(a -> medicalAppointmentMapper.mapFromEntityToDto(a))
        .orElseThrow(() -> new EntityNotFoundException(
            "MedicalAppointmentEntity not found, appointmentId: [%s]".formatted(appointmentId)
        ));
  }

  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(LocalDate date, String specialization, String location) {
    return availableAppointmentRepository.getAvailableMedicalAppointments(
            date, specialization, location).stream()
        .map(a -> availableAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  public Optional<BookingAppointmentEntity> findAppointmentByDateAndPatient(AppointmentReservationRequestDto request) {
    AvailableAppointmentEntity availableAppointment = entityManager.find(
        AvailableAppointmentEntity.class, request.getAvailableAppointmentId());
    PatientEntity patient = entityManager.find(
        PatientEntity.class, request.getPatientId());

    OffsetDateTime date = getOffsetDateTime(availableAppointment);
    return bookingAppointmentRepository.findByPatientIdAndDate(patient.getPatientId(), date);

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

    BookingAppointmentEntity bookingAppointmentEntity = bookingAppointmentRepository.save(newReservedAppointment);
    availableAppointment.setIsActive(false);
    availableAppointmentRepository.save(availableAppointment);
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

  private record MapResult(BookingAppointmentEntity bookingAppointment,
                           MedicalAppointmentEntity newMedicalAppointmentEntity) {
  }
}
