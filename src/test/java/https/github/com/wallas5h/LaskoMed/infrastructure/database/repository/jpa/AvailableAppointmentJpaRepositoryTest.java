package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

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

import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AvailableAppointmentJpaRepositoryTest {
  public AvailableAppointmentJpaRepository repository;
  private final UserJpaRepository userJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;
  private PatientJpaRepository patientJpaRepository;
  private EntityManager entityManager;

  @Test
  void thatGetAvailableMedicalAppointmentsWorks() {

    DoctorEntity doctorEntity = createDoctor();
    AvailableAppointmentEntity entity = createAvailableAppointment(doctorEntity);


    List<AvailableAppointmentEntity> availableMedicalAppointments = repository.getAvailableMedicalAppointments(
        entity.getDateAvailable(),
        entity.getDoctor().getSpecialization(),
        entity.getClinic().getAddress().getCity()
    );
    Assertions.assertThat(!availableMedicalAppointments.isEmpty());
  }

  @NotNull
  private AvailableAppointmentEntity createAvailableAppointment(DoctorEntity doctorEntity) {
    AddressEntity addressEntity = entityManager.find(AddressEntity.class, 1L);
    ClinicEntity clinicEntity = entityManager.find(ClinicEntity.class, 1L);

    AvailableAppointmentEntity entity =
        EntityFixtures.someAvailableAppointmentEntity()
            .withClinic(clinicEntity)
            .withDoctor(doctorEntity);
    return repository.saveAndFlush(entity);
  }

  @NotNull
  private DoctorEntity createDoctor() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserEntity());

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