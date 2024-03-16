package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorAvailabilityJpaRepositoryTest {

  private final UserJpaRepository userJpaRepository;
  private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;
  private EntityManager entityManager;

  @Test
  void thatFindByDoctorIdWorks() {
    DoctorAvailabilityEntity doctorAvailability = createDoctorAvailability();

    List<DoctorAvailabilityEntity> result = doctorAvailabilityJpaRepository.findByDoctorId(doctorAvailability.getDoctor().getDoctorId());

    Assertions.assertThat(result.contains(doctorAvailability));
  }

  @Test
  void thatFindPresentAvailabilitiesWorks() {
    DoctorAvailabilityEntity doctorAvailability = createDoctorAvailability();

    List<DoctorAvailabilityEntity> result = doctorAvailabilityJpaRepository.findPresentAvailabilities(
        doctorAvailability.getDoctor().getDoctorId(),
        LocalDate.now());

    Assertions.assertThat(result.contains(doctorAvailability));
  }

  @Test
  void thatFindConflictingAvailabilitiesWorks() {
    DoctorAvailabilityEntity doctorAvailability = createDoctorAvailability();

    List<DoctorAvailabilityEntity> result = doctorAvailabilityJpaRepository.findConflictingAvailabilities(
        doctorAvailability.getDoctor().getDoctorId(),
        doctorAvailability.getDateAvailable(),
        doctorAvailability.getStartTime(),
        doctorAvailability.getEndTime()
    );

    Assertions.assertThat(result.contains(doctorAvailability));
  }


  @NotNull
  private DoctorAvailabilityEntity createDoctorAvailability() {
    ClinicEntity clinicEntity = entityManager.find(ClinicEntity.class, 1L);
    DoctorEntity doctorEntity = createDoctor();

    DoctorAvailabilityEntity build = DoctorAvailabilityEntity.builder()
        .dateAvailable(LocalDate.now())
        .startTime(LocalTime.of(8, 0))
        .endTime(LocalTime.of(16, 0))
        .clinic(clinicEntity)
        .doctor(doctorEntity)
        .build();

    return doctorAvailabilityJpaRepository.saveAndFlush(build);
  }

  @NotNull
  private DoctorEntity createDoctor() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserDoctorEntity());

    DoctorEntity doctorEntity = doctorJpaRepository
        .saveAndFlush(EntityFixtures.someDoctorEntity().withAppUser(userEntity));
    return doctorEntity;
  }
}