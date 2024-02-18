package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorAvailabilityMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.MedicalAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.ValidationDoctorAvailabilityResult;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorAvailabilityRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DoctorService {
  private DoctorRepository doctorRepository;
  private DoctorAvailabilityRepository doctorAvailabilityRepository;
  private EntityManager entityManager;
  private AppointmentsService appointmentsService;

  private DoctorAvailabilityMapper doctorAvailabilityMapper;
  private DoctorMapper doctorMapper;
  private MedicalAppointmentMapper medicalAppointmentMapper;

  private static CreatedAppointmentDTO2 convertRequestToCreatedAppointmentDTO2(AvailabilityRequestDTO request) {
    Long doctorId = Long.valueOf(request.getDoctorId());
    Long clinicId = Long.valueOf(request.getClinicId());
    LocalDate dateAvailable = LocalDate.parse(request.getDateAvailable());
    LocalTime startTime = LocalTime.parse(request.getStartTime());
    LocalTime endTime = LocalTime.parse(request.getEndTime());
    CreatedAppointmentDTO2 result = new CreatedAppointmentDTO2(doctorId, clinicId, dateAvailable, startTime, endTime);
    return result;
  }

  public DoctorsDTO getDoctorsList() {
    return DoctorsDTO.of(
        doctorRepository.findAll().stream()
            .map(a -> doctorMapper.mapFromEntityToDto(a))
            .toList()
    );
  }

  public DoctorDTO getDoctorDetails(Long doctorId) {
    return doctorRepository.findAllByDoctorId(doctorId)
        .map(a -> doctorMapper.mapFromEntityToDto(a))
        .orElseThrow(() -> new EntityNotFoundException(
            "Doctor details not found, doctorId: [%s]".formatted(doctorId)
        ));
  }

  public List<DoctorAvailabilityDTO> getDoctorAvailabilities(Long doctorId) {
    return doctorAvailabilityRepository.findByDoctorId(doctorId).stream()
        .map(a -> doctorAvailabilityMapper.mapFromEntityToDto(a))
        .toList();
  }

  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(Long doctorId) {
    OffsetDateTime offsetDateTime= OffsetDateTime.of(
        LocalDate.now(), LocalTime.now().minusHours(1), ZoneOffset.UTC);
    OffsetDateTime toDateTime=offsetDateTime.plusDays(30);
    return appointmentsService.getDoctorAppointmentsByDateRange(doctorId, offsetDateTime, toDateTime);
  }

  public List<DoctorAvailabilityDTO> getDoctorPresentAvailabilities(Long doctorId) {
    return doctorAvailabilityRepository.findPresentAvailabilities(doctorId, LocalDate.now())
        .stream()
        .map(a -> doctorAvailabilityMapper.mapFromEntityToDto(a))
        .toList();
  }

  public DoctorAvailabilityEntity addDoctorAvailabilities(AvailabilityRequestDTO request) {
    DoctorEntity doctor = entityManager.getReference(
        DoctorEntity.class, Long.valueOf(request.getDoctorId()));
    ClinicEntity clinic = entityManager.getReference(
        ClinicEntity.class, Long.valueOf(request.getClinicId()));

    DoctorAvailabilityEntity newAvailability = DoctorAvailabilityEntity.builder()
        .doctor(doctor)
        .clinic(clinic)
        .dateAvailable(LocalDate.parse(request.getDateAvailable()))
        .startTime(LocalTime.parse(request.getStartTime()))
        .endTime(LocalTime.parse(request.getEndTime()))
        .build();

    DoctorAvailabilityEntity saved = doctorAvailabilityRepository.save(newAvailability);

    appointmentsService.createAppointmentsFromDoctorAvailabilities(convertRequestToCreatedAppointmentDTO2(request));

    return saved;
  }

  public boolean hasConflictingAvailabilities(AvailabilityRequestDTO request) {
    CreatedAppointmentDTO2 result = convertRequestToCreatedAppointmentDTO2(request);

    List<DoctorAvailabilityEntity> conflictingAvailabilities =
        doctorAvailabilityRepository.findConflictingAvailabilities(
            result.doctorId(),
            result.dateAvailable(),
            result.startTime(),
            result.endTime()
        );

    return !conflictingAvailabilities.isEmpty();
  }

  public ValidationDoctorAvailabilityResult validateAvailability(AvailabilityRequestDTO request) {
    boolean isCorrect = true;
    String message = "";
    CreatedAppointmentDTO2 result = convertRequestToCreatedAppointmentDTO2(request);

    if (result.dateAvailable() != null && result.dateAvailable().isBefore(LocalDate.now().plusDays(1))) {
      isCorrect = false;
      message = "Date must be 1 day after to the current date.";
    }

    if (result.startTime() != null && result.startTime().isBefore(LocalTime.of(7, 0, 0))) {
      isCorrect = false;
      message = "Start time must be at least 07:00:00.";
    }

    if (result.endTime() != null && result.endTime().isAfter(LocalTime.of(20, 0, 0))) {
      isCorrect = false;
      message = "End time must be before 20:00:00.";
    }
    return new ValidationDoctorAvailabilityResult(isCorrect, message);
  }

  public MedicalAppointmentDTO addMedicalAppointment(MedicalAppointmentRequestDTO request) {
    if (!appointmentsService.isExistMedicalAppointment(request)) {
      appointmentsService.addMedicalAppointment(request);
      MedicalAppointmentEntity medicalAppointmentEntity = appointmentsService.getMedicalAppointmentById(request);
      return medicalAppointmentMapper.mapFromEntityToDto(medicalAppointmentEntity);
    } else {
      return MedicalAppointmentDTO.builder()
          .errorMessage("Adding a medical appointment is not possible. Appointment already exists.")
          .build();
    }
  }

  @Transactional
  private MedicalAppointmentDTO updateMedicalAppointment(MedicalAppointmentRequestDTO request) {
    if (appointmentsService.isExistMedicalAppointment(request)) {
      appointmentsService.updateMedicalAppointment(request);
      MedicalAppointmentEntity medicalAppointmentEntity = appointmentsService.getMedicalAppointmentById(request);
      return medicalAppointmentMapper.mapFromEntityToDto(medicalAppointmentEntity);
    }else {
      return MedicalAppointmentDTO.builder()
          .errorMessage("Updating a medical appointment is not possible. Appointment do not exists.")
          .build();
    }
  }

  public record CreatedAppointmentDTO2(Long doctorId, Long clincId, LocalDate dateAvailable, LocalTime startTime,
                                       LocalTime endTime) {
  }
}
