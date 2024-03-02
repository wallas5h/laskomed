package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.MedicalAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.api.utils.ValidationDoctorAvailabilityResult;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorAvailabilityDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorJpaRepository;
import jakarta.persistence.EntityManager;
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
  private DoctorDAO doctorDAO;
  private DoctorAvailabilityDAO doctorAvailabilityDAO;

  private EntityManager entityManager;
  private AppointmentsService appointmentsService;
  private UserServiceAdvice userServiceAdvice;
  private DoctorJpaRepository doctorJpaRepository;
  private MedicalAppointmentMapper medicalAppointmentMapper;

  public static CreatedAppointmentDTO2 convertRequestToCreatedAppointmentDTO2(AvailabilityRequestDTO request) {
    Long doctorId = Long.valueOf(request.getDoctorId());
    Long clinicId = Long.valueOf(request.getClinicId());
    LocalDate dateAvailable = LocalDate.parse(request.getDateAvailable());
    LocalTime startTime = LocalTime.parse(request.getStartTime());
    LocalTime endTime = LocalTime.parse(request.getEndTime());
    return new CreatedAppointmentDTO2(doctorId, clinicId, dateAvailable, startTime, endTime);
  }

  public DoctorDTO getDoctorDetails(Long doctorId) {
    return doctorDAO.findAllByDoctorId(doctorId);

  }

  public List<DoctorAvailabilityDTO> getDoctorAvailabilities(Long doctorId) {
    return doctorAvailabilityDAO.findByDoctorId(doctorId);
  }

  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(Long doctorId) {
    OffsetDateTime offsetDateTime= OffsetDateTime.of(
        LocalDate.now(), LocalTime.now().minusHours(1), ZoneOffset.UTC);
    OffsetDateTime toDateTime=offsetDateTime.plusDays(60);
    return appointmentsService.getDoctorAppointmentsByDateRange(doctorId, offsetDateTime, toDateTime);
  }

  public List<DoctorAvailabilityDTO> getDoctorPresentAvailabilities(Long doctorId) {
    return doctorAvailabilityDAO.findPresentAvailabilities(doctorId, LocalDate.now());
  }

  public DoctorAvailabilityEntity addDoctorAvailabilities(AvailabilityRequestDTO request) {
    Long userId = userServiceAdvice.getUserId();
    DoctorEntity doctor = doctorDAO.findByUserIdOptional(userId).get();
    ClinicEntity clinic = entityManager.getReference(
        ClinicEntity.class, Long.valueOf(request.getClinicId()));

    DoctorAvailabilityEntity newAvailability = DoctorAvailabilityEntity.builder()
        .doctor(doctor)
        .clinic(clinic)
        .dateAvailable(LocalDate.parse(request.getDateAvailable()))
        .startTime(LocalTime.parse(request.getStartTime()))
        .endTime(LocalTime.parse(request.getEndTime()))
        .build();

    DoctorAvailabilityEntity saved = doctorAvailabilityDAO.save(newAvailability);

    appointmentsService.createAppointmentsFromDoctorAvailabilities(convertRequestToCreatedAppointmentDTO2(request));

    return saved;
  }

  public boolean hasConflictingAvailabilities(AvailabilityRequestDTO request) {
    CreatedAppointmentDTO2 result = convertRequestToCreatedAppointmentDTO2(request);

    List<DoctorAvailabilityEntity> conflictingAvailabilities =
        doctorAvailabilityDAO.findConflictingAvailabilities(
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
      MedicalAppointmentEntity medicalAppointmentEntity = appointmentsService.addMedicalAppointment(request);
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
      return appointmentsService.getMedicalAppointmentById(request);
    }else {
      return MedicalAppointmentDTO.builder()
          .errorMessage("Updating a medical appointment is not possible. Appointment do not exists.")
          .build();
    }
  }

  public void createDoctor(DoctorCreateRequest request, Long userId) throws Exception {

    if(doctorDAO.findByUserIdOptional(userId).isPresent()){
      throw new Exception("Doctor exist with this credentials.");
    }

    UserEntity userReference = entityManager.getReference(UserEntity.class, userId);

    DoctorEntity newDoctorEntity = DoctorEntity.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .pesel(request.getPesel())
        .specialization(request.getSpecialization())
        .pwzNumber(request.getPwzNumber())  // @TODO api zewnętrzne PWZ po pesel
        .phone(request.getPhone())
        .appUser(userReference)
        .build();

    doctorDAO.save(newDoctorEntity);
  }

  public record CreatedAppointmentDTO2(
      Long doctorId, Long clincId, LocalDate dateAvailable, LocalTime startTime, LocalTime endTime) {
  }
}
