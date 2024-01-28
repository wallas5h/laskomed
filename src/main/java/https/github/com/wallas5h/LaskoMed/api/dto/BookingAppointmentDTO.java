package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAppointmentDTO {

  private Long bookingId;
  private OffsetDateTime bookingDate;
  private String bookingStatus;
  private String type;
  private PatientDTO patientDTO;
  private DoctorDTO doctor;
  private ClinicDTO clinicDTO;
}
