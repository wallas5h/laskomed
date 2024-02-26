package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Patient", description = "Methods for patient management")
@SecurityRequirement(name="Bearer Authentication" )
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
  private UserServiceAdvice userServiceAdvice;

  @Operation(summary = "Create new patient")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "New patient created",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<?> createPatient(
      @RequestBody PatientCreateRequest request
  ){
    Map<String, Object> response = new HashMap<>();

    try{
      Long userId = userServiceAdvice.getUserId();
      patientService.createPatient(request, userId);
      response.put("response", "User added successfully");
      return ResponseEntity.ok().body(response);

    } catch (Exception e){
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @Operation(summary = "Get patient details by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the patient's deatails",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = PatientDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Patient not found",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(PATIENT_ID)
  public PatientDTO getPatientDetails(
      @Parameter(description = "patient id")
      @PathVariable Long patientId
  ) {
    return patientService.getPatientDetails(patientId);
//    @TODO dodać sprawdzenie czy user wpisuje swoje patientId
  }

  @Operation(summary = "Get patient's upcoming bookings")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the patient's upcoming appointments",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = BookingAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found patient and appointments",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(PATIENT_ID_APPOINTMENTS_UPCOMING)
  public List<BookingAppointmentDTO> getUpcomingPatientAppointments(
      @Parameter(description = "patient id")
      @PathVariable Long patientId
  ) {
    return patientService.getPatientUpcomingAppointments(patientId);
  }

//  GET /api/patients/appointments?date=2024-02-17&specialization=cardiology&location=clinicA

  @Operation(summary = "Get available appointments filtered by specialization and location")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found available appointments",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = BookingAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(PATIENTS_APPOINTMENTS)
  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(
      @Parameter(description = "doctor specialization")
      @RequestParam(required = false) String specialization,
      @Parameter(description = "city")
      @RequestParam(required = false) String location) {

    return patientService.getAvailableMedicalAppointments( specialization, location);
  }

  @Operation(summary = "Create appointment reservation by patient")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservation created",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
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

  @Operation(summary = "Change reservation status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservation changed",
       content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found patient's booking",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @PatchMapping(PATIENT_ID_APPOINTMENTS_UPCOMING)
  public ResponseEntity<?> changeBookingsStatus(
      @Parameter(description = "patient id")
      @PathVariable Long patientId,
      @Valid @RequestBody BookingAppointmentRequestDTO request
  ) {
    return patientService.changeBookingStatus(patientId, request);
  }

  @Operation(summary = "Get patient appointments history")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the patient's appointments history",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = MedicalAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found patient or bookings",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(PATIENT_ID_APPOINTMENTS_HISTORY)
  public List<MedicalAppointmentDTO> getPatientAppointments(
      @Parameter(description = "patient id")
      @PathVariable Long patientId
  ) {
    return patientService.getPatientAppointments(patientId);
  }

  @Operation(summary = "Get appointment details by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the appointment details",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = MedicalAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found booking",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(PATIENT_ID_APPOINTMENTS_HISTORY_ID)
  public MedicalAppointmentDTO getAppointmentDetails(
      @Parameter(description = "patient id")
      @PathVariable Long patientId,
      @Parameter(description = "appointment id")
      @PathVariable Long appointmentId
  ) {
    return patientService.getPatientMedicalAppointmentDetails(patientId, appointmentId);
  }

}
