package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "doctorId")
@ToString(of = {"doctorId", "name", "surname", "pesel", "specialization", "PWZ_number", "phone"})
public class Doctor {
  Long doctorId;
  String name;
  String surname;
  String pesel;
  String specialization;
  String PWZ_number;
  String phone;
  User appUser;
  Set<MedicalAppointment> appointments;
  Set<DoctorAvailability> availabilities;
  Set<CreatedAppointment> createdAppointments;

   public Set<MedicalAppointment> getAppointments() {
    return Objects.isNull(appointments) ? new HashSet<>() : appointments;
  }

   public Set<DoctorAvailability> getAvailabilities() {
    return Objects.isNull(availabilities) ? new HashSet<>() : availabilities;
  }

   public Set<CreatedAppointment> getCreatedAppointments() {
    return Objects.isNull(createdAppointments) ? new HashSet<>() : createdAppointments;
  }
}
