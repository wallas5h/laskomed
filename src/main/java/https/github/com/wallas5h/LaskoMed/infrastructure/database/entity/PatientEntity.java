package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "patientId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
public class PatientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "patient_id")
  private Integer patientId;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "pesel")
  private String pesel;

  @Column(name = "birthdate")
  private String birthdate;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "phone")
  private String phone;

  @Column(name = "medical_package")
  private String medicalPackage;

  @Column(name = "gender")
  private String gender;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private AddressEntity address;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
  private Set<MedicalAppointmentEntity> appointments;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
  private Set<BookingAppointmentEntity> bookings;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
  private Set<PrescriptionEntity> prescriptions;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
  private Set<ReferralEntity> referrals;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
  private Set<DiagnosedDiseaseEntity> diagnosedDiseases;
}
