package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(of = "availabilityId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_availability")
public class DoctorAvailabilityEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="availability_id")
  private Integer availabilityId;

  @Column(name = "date_available")  // '2024-01-27'
  private LocalDate dateAvailable;

  @Column(name = "start_time")     // "09:00:00"
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "clinic_id")
  private ClinicEntity clinic;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;
}
