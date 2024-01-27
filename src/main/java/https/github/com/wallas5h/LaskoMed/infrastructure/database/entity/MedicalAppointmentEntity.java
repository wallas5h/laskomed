package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = "appointmentId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_appointment")
public class MedicalAppointmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "appointment_id")
  private Integer appointmentId;

  @Column(name = "appointment_status")
  private String appointmentStatus;

  @Column(name = "diagnosis")
  private String diagnosis;

  @Column(name = "cost")
  private BigDecimal cost;

  @Column(name = "prescription")
  private String prescription;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "clinic_id")
  private ClinicEntity clinic;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private PatientEntity patient;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_appointment_id")
  private BookingAppointmentEntity bookingAppointment;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "doctor_id")
  private DoctorEntity doctor;

}
