package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "availabilityId")
@ToString(of = {"availabilityId", "dateAvailable", "startTime", "endTime", "clinic", "doctor" })
public class DoctorAvailability {

  Long availabilityId;
  LocalDate dateAvailable;
  LocalTime startTime;
  LocalTime endTime;
  Clinic clinic;
  Doctor doctor;
}
