package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class MedicalAppointmentJpaRepositoryTest {

  private final UserJpaRepository userJpaRepository;
  private MedicalAppointmentJpaRepository medicalRepository;
  private BookingAppointmentJpaRepository bookingRepository;
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;
  private PatientJpaRepository patientJpaRepository;
  private EntityManager entityManager;

  @Test
  void thatFindByBookingId() {
    MedicalAppointmentEntity medicalAppointment = createMedicalAppointment();
    Optional<MedicalAppointmentEntity> result = medicalRepository.findByBookingId(medicalAppointment.getBookingAppointment().getBookingId());

    Assertions.assertThat(medicalAppointment.equals(result));
  }

  @Test
  void thatFindByPatientId() {
    MedicalAppointmentEntity medicalAppointment = createMedicalAppointment();
    List<MedicalAppointmentEntity> result = medicalRepository.findByPatientId(medicalAppointment.getPatient().getPatientId());

    Assertions.assertThat(result.contains(medicalAppointment));
  }

  @Test
  void thatFindByPatientIdAndSpecialization() {
    MedicalAppointmentEntity medicalAppointment = createMedicalAppointment();
    Optional<MedicalAppointmentEntity> result = medicalRepository.findByPatientIdAndSpecialization(
        medicalAppointment.getPatient().getPatientId(),
        medicalAppointment.getDoctor().getSpecialization()
    );

    Assertions.assertThat(medicalAppointment.equals(result.get()));
  }

  @Test
  void thatUpdateByBookingId() {
    MedicalAppointmentEntity medicalAppointment = createMedicalAppointment();
    int updated = medicalRepository.updateByBookingId(
        medicalAppointment.getBookingAppointment().getBookingId(),
        medicalAppointment.withDoctorComment("test2")
    );

    Assertions.assertThat(updated>0);
  }


  private static OffsetDateTime getOffsetDateTime(AvailableAppointmentEntity availableAppointment) {
    return OffsetDateTime.of(
        availableAppointment.getDateAvailable(),
        availableAppointment.getStartTime(),
        ZoneOffset.UTC);
  }


  @NotNull
  private MedicalAppointmentEntity createMedicalAppointment() {
    BookingAppointmentEntity booking = createBooking();

    MedicalAppointmentEntity medicalAppointmentEntity = MedicalAppointmentEntity.builder()
        .appointmentStatus(EnumsContainer.AppointmentStatus.COMPLETED.name())
        .diagnosis("test")
        .doctorComment("test")
        .cost(BigDecimal.valueOf(100))
        .prescription("test")
        .clinic(booking.getClinic())
        .patient(booking.getPatient())
        .bookingAppointment(booking)
        .doctor(booking.getDoctor())
        .build();

    return medicalRepository.saveAndFlush(medicalAppointmentEntity);
  }

  @NotNull
  private BookingAppointmentEntity createBooking() {
    List<AvailableAppointmentEntity> availableAppointments = createAvailableAppointments();
    AvailableAppointmentEntity availableAppointment = availableAppointments.get(1);
    OffsetDateTime bookingDate = getOffsetDateTime(availableAppointment);

    BookingAppointmentEntity newReservedAppointment = BookingAppointmentEntity.builder()
        .bookingDate(bookingDate)
        .doctor(availableAppointment.getDoctor())
        .clinic(availableAppointment.getClinic())
        .availableAppointment(availableAppointment)
        .type(EnumsContainer.AppointmentType.FACILITY.name())
        .patient(createPatient())
        .bookingStatus(EnumsContainer.BookingStatus.PENDING.name())
        .build();

    return bookingRepository.saveAndFlush(newReservedAppointment);
  }

  @NotNull
  private List<AvailableAppointmentEntity> createAvailableAppointments() {
    LocalDate dateAvailable = LocalDate.now().plusDays(2);
    LocalTime startTime = LocalTime.of(8, 0, 0);
    LocalTime endTime = LocalTime.of(16, 0, 0);

    ClinicEntity clinicEntity = entityManager.find(ClinicEntity.class, 1L);
    DoctorEntity doctorEntity = createDoctor();

    Duration appointmentDuration = Duration.ofMinutes(20);
    LocalTime currentTime = startTime;

    while (currentTime.plus(appointmentDuration).isBefore(endTime) || currentTime.plus(appointmentDuration).equals(endTime)) {

      AvailableAppointmentEntity newCreatedAppointment = AvailableAppointmentEntity.builder()
          .doctor(doctorEntity)
          .clinic(clinicEntity)
          .dateAvailable(dateAvailable)
          .startTime(currentTime)
          .endTime(currentTime.plus(appointmentDuration))
          .isActive(true)
          .build();

      availableAppointmentJpaRepository.saveAndFlush(newCreatedAppointment);

      currentTime = currentTime.plus(appointmentDuration);
    }
    return availableAppointmentJpaRepository.findAll();
  }

  @NotNull
  private DoctorEntity createDoctor() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserDoctorEntity());

    DoctorEntity doctorEntity = doctorJpaRepository
        .saveAndFlush(EntityFixtures.someDoctorEntity().withAppUser(userEntity));
    return doctorEntity;
  }

  @NotNull
  private PatientEntity createPatient() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserEntity());
    PatientEntity patientEntity = patientJpaRepository.saveAndFlush(
        EntityFixtures.somePatientEntity().withAppUser(userEntity));
    return patientEntity;
  }

}