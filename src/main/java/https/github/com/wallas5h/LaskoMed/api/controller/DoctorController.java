package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import https.github.com.wallas5h.LaskoMed.infrastructure.configuration.SpringDocConfiguration;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Doctor", description = "Methods for doctor management")
@SecurityRequirement(name="Bearer Authentication" )
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

  @Operation(summary = "Create new doctor")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "New doctor created",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DoctorDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<Map<String, String>> createDoctor(
      @RequestBody DoctorCreateRequest request
  ){
    Map<String, String> response = new HashMap<>();

    try{
      Long userId = userServiceAdvice.getUserId();

      doctorService.createDoctor(request, userId);
      response.put("message", "Doctor added successfully");
      return ResponseEntity.ok().body(response);

    } catch (Exception e){
      if (e instanceof ServerErrorException) {
        response.put("error", "Server error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @Operation(summary = "Get doctor details by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the doctor's deatails",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DoctorDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Doctor not found",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(DOCTORS_ID)
  public DoctorDTO doctorDetails(
      @Parameter(description = "doctor id")
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorDetails(doctorId);
  }

  @Operation(summary = "Get doctor list availabilities")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the doctor's availabilities",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DoctorAvailabilityDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found doctor and availabilities",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(DOCTORS_ID_AVAILABILITIES)
  public List<DoctorAvailabilityDTO> getDoctorAllAvailabilities(
      @Parameter(description = "doctor id")
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorAvailabilities(doctorId);
  }

  @Operation(summary = "Get doctor's list of present availabilities")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the doctor's present availabilities",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DoctorAvailabilityDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found doctor and availabilities",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(DOCTORS_ID_AVAILABILITIES_PRESENT)
  public List<DoctorAvailabilityDTO> getDoctorPresentAvailabilities(
      @Parameter(description = "doctor id")
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorPresentAvailabilities(doctorId);
  }


  @Operation(summary = "Get doctor's upcoming appointments")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the doctor's upcoming appointments",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = BookingAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found doctor and appointments",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(DOCTORS_ID_APPOINTMENTS_UPCOMING)
  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(
      @Parameter(description = "doctor id")
      @PathVariable Long doctorId
  ) {
    return doctorService.getDoctorUpcomingAppointments(doctorId);
  }

  @Operation(summary = "Create doctor's availabilities")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Created",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DoctorDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Not found",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
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
@Operation(summary = "Create medical appointment")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Created",
        content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = MedicalAppointmentDTO.class)) }),
    @ApiResponse(responseCode = "400", description = "Invalid input data",
        content = @Content),
    @ApiResponse(responseCode = "401", description = "Unauthorised access",
        content = @Content)
})
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

  @Operation(summary = "Get a list of patient's appointments by doctor specialization ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the appointments",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = MedicalAppointmentDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Patient not found",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(APPOINTMENTS_PATIENT)
  public List<MedicalAppointmentDTO> getPatientAppointments(
      @Parameter(description = "patient id")
      @PathVariable Long patientId,
      @RequestParam(required = false) String specialization
  ) {
    return patientService.getPatientAppointments(patientId, specialization);
  }

  @Operation(summary = "Get a list of patient's diseases ")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the patient's diseases",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = DiagnosedDiseaseDTO.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Patient not found",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @GetMapping(APPOINTMENTS_PATIENT_DISEASE)
  public List<DiagnosedDiseaseDTO> getPatientDiseases(
      @Parameter(description = "patient id")
      @PathVariable Long patientId
  ) {
    return patientService.getPatientDiseases(patientId);
  }

}
