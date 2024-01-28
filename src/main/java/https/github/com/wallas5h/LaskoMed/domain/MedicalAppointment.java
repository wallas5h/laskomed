package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.math.BigDecimal;

@With
@Value
@Builder
@EqualsAndHashCode(of = "appointmentId")
@ToString(of =
    {"appointmentId", "appointmentStatus", "diagnosis", "cost", "prescription", "clinic", "patient", "bookingAppointment", "doctor"})
public class MedicalAppointment {
  Long appointmentId;
  String appointmentStatus;
  String diagnosis;
  String doctorComment;
  BigDecimal cost;
  String prescription;
  Clinic clinic;
  Patient patient;
  BookingAppointment bookingAppointment;
  Doctor doctor;
}
