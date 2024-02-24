package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(DoctorController.DOCTORS)
@AllArgsConstructor
public class DoctorController {
  public static final String DOCTORS = "/doctors";
  public static final String DOCTORS_ID = "/{doctorId}";
  public static final String DOCTORS_ID_AVAILABILITIES = "/{doctorId}/availabilities";
  public static final String DOCTORS_ID_AVAILABILITIES_PRESENT = "/{doctorId}/availabilities/present";
  public static final String DOCTORS_AVAILABILITIES = "/availabilities";
  public static final String DOCTORS_AVAILABILITIES_CREATE = "/availabilities/create";
  public static final String DOCTORS_ID_APPOINTMENTS = "/{doctorId}/appointments";
  public static final String APPOINTMENTS_PATIENT = "/appointments/patient/{patientId}";
  public static final String APPOINTMENTS_PATIENT_DISEASE = "/appointments/patient/{patientId}/disease";
  public static final String DOCTORS_PROCESSING_APPOINTMENT = "/{doctorId}/appointments/processing";
  public static final String DOCTORS_ID_APPOINTMENTS_UPCOMING = "/{doctorId}/appointments/upcoming";


  private DoctorService doctorService;
  private PatientService patientService;
  private UserServiceAdvice userServiceAdvice;

  @PostMapping
  public ResponseEntity<?> createPatient(
      @RequestBody DoctorCreateRequest request
  ){
    Map<String, Object> response = new HashMap<>();

    try{
      Long userId = userServiceAdvice.getUserId();

      doctorService.createDoctor(request, userId);
      response.put("response", "Doctor added successfully");
      return ResponseEntity.ok().body(response);

    } catch (Exception e){
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @GetMapping(DOCTORS_ID)
  public DoctorDTO doctorDetails(
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorDetails(doctorId);
  }

  @GetMapping(DOCTORS_ID_AVAILABILITIES)
  public List<DoctorAvailabilityDTO> getDoctorAllAvailabilities(
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorAvailabilities(doctorId);
  }
  @GetMapping(DOCTORS_ID_AVAILABILITIES_PRESENT)
  public List<DoctorAvailabilityDTO> getDoctorPresentAvailabilities(
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorPresentAvailabilities(doctorId);
  }
  @GetMapping(DOCTORS_ID_APPOINTMENTS_UPCOMING)
  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorUpcomingAppointments(doctorId);
  }

  @PostMapping(
      value = DOCTORS_AVAILABILITIES_CREATE,
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE
      }
  )
  public ResponseEntity<String> addDoctorAvailabilities(
      @Valid @RequestBody AvailabilityRequestDTO request
  ) {
    if (!doctorService.validateAvailability(request).isCorrect()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(doctorService.validateAvailability(request).getMessage());
    }

    if (doctorService.hasConflictingAvailabilities(request)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Your availabilities can not be added. There is a conflict of yours available dates or hours");
    }

    DoctorAvailabilityEntity newAvailability = doctorService.addDoctorAvailabilities(request);

    if (Objects.isNull(newAvailability.getAvailabilityId())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
    }
    return ResponseEntity.ok("Availability added successfully");
  }

//  @TODO podwójne zapytanie wywołyje błąd, dodać opcję update
  @PostMapping(
      value = DOCTORS_PROCESSING_APPOINTMENT,
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
      }
  )
  public MedicalAppointmentDTO addMedicalAppointment(
      @Valid @RequestBody MedicalAppointmentRequestDTO request
  ) {
    return doctorService.addMedicalAppointment(request);
  }

  @GetMapping(APPOINTMENTS_PATIENT)
  public List<MedicalAppointmentDTO> getPatientAppointments(
      @PathVariable Long patientId,
      @RequestParam(required = false) String specialization
  ) {
    return patientService.getPatientAppointments(patientId, specialization);
  }
  @GetMapping(APPOINTMENTS_PATIENT_DISEASE)
  public List<DiagnosedDiseaseDTO> getPatientDiseases(
      @PathVariable Long patientId
  ) {
    return patientService.getPatientDiseases(patientId);
  }

}
