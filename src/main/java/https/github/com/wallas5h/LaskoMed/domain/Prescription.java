package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import lombok.*;

import java.time.LocalDate;

@With
@Value
@Builder
@EqualsAndHashCode(of = "prescriptionId")
@ToString(of =
    {"prescriptionId", "prescriptionNumber", "prescriptionIssueDate", "prescriptionCompletionDate",
    "nfz_department", "patientAdditionalRightsCode", "medicationName", "dosage", "instructions", "patient",
    "doctor", "clinic" })
public class Prescription {
  Long prescriptionId;
  String prescriptionNumber;
  LocalDate prescriptionIssueDate;
  LocalDate prescriptionCompletionDate;
  String nfz_department;
  String patientAdditionalRightsCode;
  String medicationName;
  String dosage;
  String instructions;
  PatientEntity patient;
  DoctorEntity doctor;
  ClinicEntity clinic;
}
