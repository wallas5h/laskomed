package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorAvailabilityMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.ValidationDoctorAvailabilityResult;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorAvailabilityRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

  public DoctorsDTO getDoctorsList(){
    return DoctorsDTO.of(
        doctorRepository.findAll().stream()
        .map(a-> doctorMapper.mapFromEntityToDto(a))
        .toList()
    );
  }

  public DoctorDTO getDoctorDetails(Long doctorId) {
    return doctorRepository.findAllByDoctorId( doctorId)
        .map(a-> doctorMapper.mapFromEntityToDto(a))
        .orElseThrow(() -> new EntityNotFoundException(
            "Doctor details not found, doctorId: [%s]".formatted(doctorId)
        ));
  }


  public List<DoctorAvailabilityDTO> getDoctorAvailabilities(Long doctorId) {
    return doctorAvailabilityRepository.findByDoctorId(doctorId).stream()
        .map(a-> doctorAvailabilityMapper.mapFromEntityToDto(a))
        .toList();
  }

  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(Long doctorId) {
    return appointmentsService.getDoctorUpcomingAppointments(doctorId);
  }

  public List<DoctorAvailabilityDTO> getDoctorPresentAvailabilities(Long doctorId) {
    return doctorAvailabilityRepository.findPresentAvailabilities(doctorId, LocalDate.now())
        .stream()
        .map(a-> doctorAvailabilityMapper.mapFromEntityToDto(a))
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

//    @TODO podzielić czas dostępności na createdAppointments co 20 min

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
    boolean isCorrect= true;
    String message="";
    CreatedAppointmentDTO2 result = convertRequestToCreatedAppointmentDTO2(request);

    // Sprawdź czy dateAvailable jest po obecnej dacie
    if (result.dateAvailable() != null && result.dateAvailable().isBefore(LocalDate.now().plusDays(1))) {
      isCorrect=false;
      message="Date must be 1 day after to the current date.";
    }

    // Sprawdź czy startTime jest najwcześniej od 07:00:00
    if (result.startTime() != null && result.startTime().isBefore(LocalTime.of(7, 0, 0))) {
      isCorrect=false;
      message="Start time must be at least 07:00:00.";
    }

    // Sprawdź czy endTime nie jest po 20:00:00
    if (result.endTime() != null && result.endTime().isAfter(LocalTime.of(20, 0, 0))) {
      isCorrect=false;
      message="End time must be before 20:00:00.";
    }
    return new ValidationDoctorAvailabilityResult(isCorrect, message);
  }

  private static CreatedAppointmentDTO2 convertRequestToCreatedAppointmentDTO2(AvailabilityRequestDTO request) {
    Long doctorId = Long.valueOf(request.getDoctorId());
    Long clinicId = Long.valueOf(request.getClinicId());
    LocalDate dateAvailable = LocalDate.parse(request.getDateAvailable());
    LocalTime startTime = LocalTime.parse(request.getStartTime());
    LocalTime endTime = LocalTime.parse(request.getEndTime());
    CreatedAppointmentDTO2 result = new CreatedAppointmentDTO2(doctorId, clinicId, dateAvailable, startTime, endTime);
    return result;
  }

  public record CreatedAppointmentDTO2(Long doctorId, Long clincId, LocalDate dateAvailable, LocalTime startTime, LocalTime endTime) {
  }
}
