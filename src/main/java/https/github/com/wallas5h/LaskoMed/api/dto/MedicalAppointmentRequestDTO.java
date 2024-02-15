package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentRequestDTO {
  private String appointmentStatus;
  private String diagnosis;
  private String doctorComment;
  private String prescription;
  private String clinicId;
  private String patientId;
  private String bookingAppointmentId;
  private String doctorId;
}
