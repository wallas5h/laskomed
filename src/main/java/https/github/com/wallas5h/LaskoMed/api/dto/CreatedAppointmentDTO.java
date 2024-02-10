package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatedAppointmentDTO {

  private Long createdAppointmentId;
  private LocalDate dateAvailable;
  private LocalTime startTime;
  private LocalTime endTime;
  private ClinicDtoId clinic;
  private DoctorsDtoId doctor;
}
