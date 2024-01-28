package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "referralId")
@ToString(of =
    {"referralId", "referralDate", "referralPurpose", "referralDiagnosis", "address", "doctor",
    "clinic", "patient" })
public class Referral {

  Long referralId;
  OffsetDateTime referralDate;
  String referralPurpose;
  String referralDiagnosis;
  Address address;
  Doctor doctor;
  Clinic clinic;
  Patient patient;
}
