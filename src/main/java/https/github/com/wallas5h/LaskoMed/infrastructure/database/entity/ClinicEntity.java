package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "clinicId")
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clinic")
public class ClinicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="clinic_id")
  private Long clinicId;

  @Column(name="name")
  private String name;

  @Column(name="regon")
  private String regon;

  @Column(name="nip")
  private String nip;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private AddressEntity address;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "clinic")
  private Set<MedicalAppointmentEntity> appointments;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "clinic")
  private Set<AvailableAppointmentEntity> createdAppointments;
}
