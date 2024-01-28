package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "clinicId")
@ToString(of = {"clinicId", "name", "regon", "nip", "address"})
public class Clinic {
  Long clinicId;
  String name;
  String regon;
  String nip;
  Address address;
  Set<MedicalAppointment> appointments;

  public Set<MedicalAppointment> getAppointments() {
    return Objects.isNull(appointments) ? new HashSet<>() : appointments;
  }
}
