package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PatientController.PATIENTS)
@AllArgsConstructor
public class PatientController {
  public static final String PATIENTS = "/patients";
  public static final String PATIENT_ID = "/{patientId}";
  public static final String PATIENTS_APPOINTMENTS = "/appointments";
  public static final String PATIENTS_APPOINTMENTS_RESERVE = "/appointments/reserve";
  public static final String PATIENT_ID_APPOINTMENTS_UPCOMING = "/{patientId}/appointments/upcoming";
  public static final String PATIENT_ID_APPOINTMENTS_UPCOMING_ID = "/{patientId}/appointments/upcoming/{appointmentId}";
  public static final String PATIENT_ID_APPOINTMENTS_HISTORY = "/{patientId}/appointments/history";
  public static final String PATIENT_ID_APPOINTMENTS_HISTORY_ID = "/{patientId}/appointments/history/{appointmentId}";

  private PatientService patientService;


  @GetMapping(PATIENT_ID)
  public PatientDTO getPatientDetails(
      @PathVariable Long patientId
  ) {
    return patientService.getPatientDetails(patientId);
//    @TODO dodać sprawdzenie czy user wpisuje swoje patientId
  }


  @GetMapping(PATIENT_ID_APPOINTMENTS_UPCOMING)
  public List<BookingAppointmentDTO> getUpcomingPatientAppointments(
      @PathVariable Long patientId
  ) {
    return patientService.getPatientUpcomingAppointments(patientId);
  }

//  GET /api/patients/appointments?date=2024-02-17&specialization=cardiology&location=clinicA

  @GetMapping(PATIENTS_APPOINTMENTS)
  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(
      @RequestParam(required = false) String specialization,
      @RequestParam(required = false) String location) {

    return patientService.getAvailableMedicalAppointments( specialization, location);
  }

  @PostMapping(
      value = PATIENTS_APPOINTMENTS_RESERVE,
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE
      }
  )
  public ResponseEntity<?> reserveAppointment(
      @Valid @RequestBody AppointmentReservationRequestDto request) {
    return patientService.reserveAppointment(request);
  }

  @PatchMapping(PATIENT_ID_APPOINTMENTS_UPCOMING)
  public ResponseEntity<?> changeBookingsStatus(
      @PathVariable Long patientId,
      @Valid @RequestBody BookingAppointmentRequestDTO request
  ) {
    return patientService.changeBookingStatus(patientId, request);
  }

  @GetMapping(PATIENT_ID_APPOINTMENTS_HISTORY)
  public List<MedicalAppointmentDTO> getPatientAppointments(
      @PathVariable Long patientId
  ) {
    return patientService.getPatientAppointments(patientId);
  }

  @GetMapping(PATIENT_ID_APPOINTMENTS_HISTORY_ID)
  public MedicalAppointmentDTO getAppointmentDetails(
      @PathVariable Long patientId,
      @PathVariable Long appointmentId
  ) {
    return patientService.getPatientMedicalAppointmentDetails(patientId, appointmentId);
  }

}
