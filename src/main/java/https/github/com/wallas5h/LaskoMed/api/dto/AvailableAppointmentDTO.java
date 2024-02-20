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
public class AvailableAppointmentDTO {

  private Long availableAppointmentId;
  private LocalDate dateAvailable;
  private LocalTime startTime;
  private LocalTime endTime;
  private Boolean isActive;
  private ClinicDtoId clinic;
  private DoctorsDtoId doctor;
}
