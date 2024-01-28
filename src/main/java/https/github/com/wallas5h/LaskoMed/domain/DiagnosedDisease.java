package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "diagnosedDiseaseId")
@ToString(of = {"diagnosedDiseaseId", "diagnosisDate", "diseaseName", "description", "patient", "doctor"})
public class DiagnosedDisease {

  Long diagnosedDiseaseId;
  OffsetDateTime diagnosisDate;
  String diseaseName;
  String description;
  PatientEntity patient;
  AddressEntity doctor;
}
