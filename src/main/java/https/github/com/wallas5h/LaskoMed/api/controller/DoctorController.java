package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.AvailabilityRequestDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorsDTO;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(DoctorController.DOCTORS)
@AllArgsConstructor
public class DoctorController {
  public static final String DOCTORS = "/doctors";
  public static final String DOCTORS_ID = "/{doctorId}";
  public static final String DOCTORS_ID_APPOINTMENTS = "/{doctorId}/appointments";
  public static final String DOCTORS_ID_AVAILABILITIES = "/{doctorId}/availabilities";
  public static final String DOCTORS_ID_AVAILABILITIES_PRESENT = "/{doctorId}/availabilities/present";
  public static final String DOCTORS_ID_AVAILABILITIES_ARCHIVED = "/{doctorId}/availabilities/archived";
  public static final String DOCTORS_AVAILABILITIES = "/availabilities";
  public static final String DOCTORS_AVAILABILITIES_CREATE = "/availabilities/create";


  private DoctorService doctorService;

  @GetMapping
  public DoctorsDTO patientsList() {
    return doctorService.getDoctorsList();
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

}
