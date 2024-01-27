package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "doctorId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor")
public class DoctorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="doctor_id")
  private Long doctorId;

  @Column(name="name")
  private String name;

  @Column(name="surname")
  private String surname;

  @Column(name="pesel")
  private String pesel;

  @Column(name="specialization")
  private String specialization;

  @Column(name="PWZ_number")
  private String PWZ_number;

  @Column(name="phone")
  private String phone;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
  private Set<MedicalAppointmentEntity> appointments;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
  private Set<DoctorAvailabilityEntity> availabilities;

}
