package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
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
  AddressEntity address;
  DoctorEntity doctor;
  ClinicEntity clinic;
  PatientEntity patient;
}
