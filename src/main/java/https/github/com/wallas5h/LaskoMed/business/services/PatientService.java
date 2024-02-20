package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.DiagnosedDiseaseMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {
  private PatientRepository patientRepository;
  private AvailableAppointmentRepository availableAppointmentRepository;
  private DiagnosesDiseaseRepository diseaseRepository;

  private AppointmentsService appointmentsService;
  private PatientMapper patientMapper;
  private DiagnosedDiseaseMapper diagnosedDiseaseMapper;

  public PatientsDTO getPatientsList() {
    return PatientsDTO.of(patientRepository.findAll().stream()
        .map(a -> patientMapper.mapFromEntityToDto(a))
        .toList()
    );
  }

  public PatientDTO getPatientDetails(Long patientId) {
    Optional<PatientEntity> entity = patientRepository.findById(patientId);

    return entity.map(patientEntity -> patientMapper.mapFromEntityToDto(patientEntity))
        .orElseThrow(() -> new EntityNotFoundException(
            "PatientEntity not found, patientId: [%s]".formatted(patientId)
        ));

  }

  public List<BookingAppointmentDTO> getPatientUpcomingAppointments(Long patientId) {
    return appointmentsService.getPatientUpcomingBookings(patientId);
  }

  public ResponseEntity<?> changeBookingStatus(Long patientId, BookingAppointmentRequestDTO request) {
    int changedBookingStatus = appointmentsService.changeBookingStatus(patientId, request);
    if (changedBookingStatus < 1) {
      return ResponseEntity.status(400).body("Incorrect input data");
    } else {
      if (request.getBookingStatus().toUpperCase().equals(EnumsContainer.BookingStatus.CANCELLED.name())){
        return ResponseEntity.ok("Booking canceled successfully");
      }
      return ResponseEntity.ok("Booking changed successfully");
    }
  }

  public List<MedicalAppointmentDTO> getPatientAppointments(Long patientId) {
    return appointmentsService.getPatientMedicalAppointments(patientId);
  }
  public List<MedicalAppointmentDTO> getPatientAppointments(Long patientId, String specialization) {
    specialization = specialization == null ? "" : specialization;
    
    if (specialization.isEmpty()){
      return appointmentsService.getPatientMedicalAppointments(patientId);
    }
    return appointmentsService.getPatientMedicalAppointments(patientId, specialization);
  }

  public boolean isBookingStatusValid(BookingAppointmentRequestDTO request) {
    try {
      EnumsContainer.BookingStatus status = EnumsContainer.BookingStatus.valueOf(request.getBookingStatus().toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public MedicalAppointmentDTO getPatientMedicalAppointmentDetails(Long patientId, Long appointmentId) {
    return appointmentsService.getPatientMedicalAppointmentDeatails(appointmentId);
  }

  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(String specialization, String location) {
    LocalDate date= LocalDate.now();
    specialization = specialization == null ? "" : specialization;
    location = location == null ? "" : location;

    return appointmentsService.getAvailableMedicalAppointments(date, specialization, location);
  }

  public ResponseEntity<?> reserveAppointment(AppointmentReservationRequestDto request) {
    if (validatePatientDateTimeCollisionAppointments(request)){
      return ResponseEntity.status(400).body("Patient already has an appointment at this time.");
    };

    BookingAppointmentEntity newBookingAppointment = appointmentsService.reserveAppointment(request);
    if (newBookingAppointment==null){
      return ResponseEntity.status(404).body(
          "The appointment is no longer available. The appointment has been reserved by another patient.");
    }
    if (newBookingAppointment.getBookingId() != null) {
      return ResponseEntity.ok("Booking appointment successfully");
    } else {
      return ResponseEntity.status(500).body("Sorry try later");
    }
  }

  private boolean validatePatientDateTimeCollisionAppointments(AppointmentReservationRequestDto request) {
    boolean isCollision=false;
    Optional<BookingAppointmentEntity> appointmentByDateAndPatient = appointmentsService.findAppointmentByDateAndPatient(request);
    if (appointmentByDateAndPatient.isPresent()){
      isCollision=true ;
    }
    return isCollision;
  }

  public List<DiagnosedDiseaseDTO> getPatientDiseases(Long patientId) {
    return diseaseRepository.findByPatientId(patientId).stream()
        .map(diagnosedDiseaseMapper::mapFromEntityToDto)
        .toList();
  }
}
