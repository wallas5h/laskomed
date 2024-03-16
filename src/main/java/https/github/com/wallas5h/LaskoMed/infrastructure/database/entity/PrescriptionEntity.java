package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = "prescriptionId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription")
public class PrescriptionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "prescription_id")
  private Long prescriptionId;

  @Column(name = "prescription_number")
  private String prescriptionNumber;

  @Column(name = "prescription_issue_date")
  private LocalDate prescriptionIssueDate;

  @Column(name = "prescription_completion_date")
  private LocalDate prescriptionCompletionDate;

  @Column(name = "nfz_department")
  private String nfz_department;

  @Column(name = "patient_additional_rights_code")
  private String patientAdditionalRightsCode;

  @Column(name = "medication_name")
  private String medicationName;

  @Column(name = "dosage")
  private String dosage;

  @Column(name = "instructions")
  private String instructions;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "clinic_id")
  private ClinicEntity clinic;
}
