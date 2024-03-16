package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "patientId")
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
public class PatientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "patient_id")
  private Long patientId;

  @NotBlank
  @Size(max = 32)
  @Column(name = "name")
  private String name;

  @NotBlank
  @Size(max = 32)
  @Column(name = "surname")
  private String surname;

  @NotBlank
  @Size(max = 32)
  @Column(name = "pesel")
  private String pesel;

  @NotNull
  @Column(name = "birthdate")
  private LocalDate birthdate;

  @NotBlank
  @Email
  @Size(max = 32)
  @Column(name = "email")
  private String email;

  @NotBlank
  @Size(max = 32)
  @Column(name = "phone")
  private String phone;

  @NotBlank
  @Pattern(regexp = "^(premium|standard)$")
  @Column(name = "medical_package")
  private String medicalPackage;

  @NotBlank
  @Size(max = 20)
  @Column(name = "gender")
  private String gender;

  @NotNull
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private AddressEntity address;

  @NotNull
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private UserEntity appUser;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
  private Set<MedicalAppointmentEntity> appointments;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
  private Set<BookingAppointmentEntity> bookings;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
  private Set<PrescriptionEntity> prescriptions;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
  private Set<ReferralEntity> referrals;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
  private Set<DiagnosedDiseaseEntity> diagnosedDiseases;
}
