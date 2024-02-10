package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "createdAppointmentId")
@ToString(of = {"createdAppointmentId" })
public class CreatedAppointment {

  Long createdAppointmentId;
  LocalDate dateAvailable;
  LocalTime startTime;
  LocalTime endTime;
  Clinic clinic;
  Doctor doctor;
}
