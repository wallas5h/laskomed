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

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class BookingAppointmentJpaRepositoryTest {
  private BookingAppointmentJpaRepository bookingRepository;
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;
  private PatientJpaRepository patientJpaRepository;
  private EntityManager entityManager;

@Test
void thatFindByDoctorIdWorks(){
  BookingAppointmentEntity booking = createBooking();

  List<BookingAppointmentEntity> bookingResult = bookingRepository.findByDoctorId(booking.getDoctor().getDoctorId());
  Assertions.assertThat(booking.equals(bookingResult));
}

@Test
void thatFindByDoctorIdAndDateRangeWorks(){
  BookingAppointmentEntity booking = createBooking();

  List<BookingAppointmentEntity> bookingResult = bookingRepository.findByDoctorIdAndDateRange(
      booking.getDoctor().getDoctorId(),
      booking.getBookingDate(),
      booking.getBookingDate().plusDays(1)
          );
  Assertions.assertThat(booking.equals(bookingResult));
}

@Test
void thatFindByPatientIdWorks(){
  BookingAppointmentEntity booking = createBooking();

  List<BookingAppointmentEntity> bookingResult = bookingRepository.findByPatientId(booking.getPatient().getPatientId());
  Assertions.assertThat(booking.equals(bookingResult));
}

@Test
void thatFindByPatientIdAndDate(){
  BookingAppointmentEntity booking = createBooking();
  Optional<BookingAppointmentEntity> bookingResult = bookingRepository.findByPatientIdAndDate(
      booking.getPatient().getPatientId(),
      booking.getBookingDate()
  );
  Assertions.assertThat(booking.equals(bookingResult.get()));
}

@Test
void thatUpdateBookingStatusWorks(){
  BookingAppointmentEntity booking = createBooking();

  int updated = bookingRepository.updateBookingStatus(booking.getBookingId(), EnumsContainer.BookingStatus.COMPLETED.name());
  Assertions.assertThat(updated>0);
}

@Test
void thatUpdateBookingStatusByPatientWorks(){
  BookingAppointmentEntity booking = createBooking();

  int updated = bookingRepository.updateBookingStatusByPatient(
      booking.getBookingId(),
      EnumsContainer.BookingStatus.CANCELLED.name(),
      booking.getPatient().getPatientId(),
      booking.getBookingDate()
  );
  Assertions.assertThat(updated>0);
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

  private static OffsetDateTime getOffsetDateTime(AvailableAppointmentEntity availableAppointment) {
    return OffsetDateTime.of(
        availableAppointment.getDateAvailable(),
        availableAppointment.getStartTime(),
        ZoneOffset.UTC);
  }

  @NotNull
  private List<AvailableAppointmentEntity> createAvailableAppointments() {
    LocalDate dateAvailable = LocalDate.now().plusDays(2);
    LocalTime startTime = LocalTime.of(8,0,0);
    LocalTime endTime = LocalTime.of(16,0,0);

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