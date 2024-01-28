package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
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
  PatientEntity patient;
  DoctorEntity doctor;
  ClinicEntity clinic;
}
