package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentDTO {
  private Long appointmentId;
  private String appointmentStatus;
  private String diagnosis;
  private String doctorComment;
  private BigDecimal cost;
  private String prescription;
  private ClinicDTO clinic;
  private PatientDTO patient;
  private BookingAppointmentDTO bookingAppointment;
  private DoctorDTO doctor;
}
