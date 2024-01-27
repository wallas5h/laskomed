package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "diagnosedDiseaseId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnosed_disease")
public class DiagnosedDiseaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "diagnosed_disease_id")
  private Integer diagnosedDiseaseId;

  @Column(name = "diagnosis_date")
  private OffsetDateTime diagnosisDate;

  @Column(name = "disease_name")
  private String diseaseName;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "doctor_id")
  private AddressEntity doctor;
}
