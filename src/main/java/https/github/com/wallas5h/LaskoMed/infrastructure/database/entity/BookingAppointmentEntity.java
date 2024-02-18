package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "bookingId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking_appointment")
public class BookingAppointmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_id")
  private Long bookingId;

  @Column(name = "booking_date")
  private OffsetDateTime bookingDate;

  @Column(name = "booking_status")
  private String bookingStatus;

  @Column(name = "appointment_type")
  private String type;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "clinic_id")
  private ClinicEntity clinic;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "available_appointment_id")
  private AvailableAppointmentEntity availableAppointment;
}
