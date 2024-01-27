package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "referralId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription")
public class ReferralEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "referral_id")
  private String referralId;

  @Column(name = "referral_date")
  private OffsetDateTime referralDate;

  @Column(name = "referral_purpose")
  private String referralPurpose;

  @Column(name = "referral_diagnosis")
  private String referralDiagnosis;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private AddressEntity address;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "referring_doctor_id")
  private DoctorEntity doctor;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "referring_clinic_id")
  private ClinicEntity clinic;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

}
