package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "bookingId")
@ToString(of = {"bookingId", "bookingDate", "bookingStatus", "type", "patient", "doctor", "clinic"})
public class BookingAppointment {

  Long bookingId;
  OffsetDateTime bookingDate;
  String bookingStatus;
  String type;
  Patient patient;
  Doctor doctor;
  Clinic clinic;
}
