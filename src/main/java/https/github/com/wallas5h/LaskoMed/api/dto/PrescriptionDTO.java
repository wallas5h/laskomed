package https.github.com.wallas5h.LaskoMed.api.dto;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
  private Long prescriptionId;
  private String prescriptionNumber;
  private LocalDate prescriptionIssueDate;
  private LocalDate prescriptionCompletionDate;
  private String nfz_department;
  private String patientAdditionalRightsCode;
  private String medicationName;
  private String dosage;
  private String instructions;
  private PatientEntity patient;
  private DoctorEntity doctor;
  private ClinicEntity clinic;
}
