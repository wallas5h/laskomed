package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
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
  BigDecimal cost;
  String prescription;
  ClinicEntity clinic;
  PatientEntity patient;
  BookingAppointmentEntity bookingAppointment;
  DoctorEntity doctor;
}
