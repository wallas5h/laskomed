package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.Set;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DiagnosesDiseaseJpaRepositoryTest {
  private DiagnosesDiseaseJpaRepository diagnosesDiseaseJpaRepository;
  private final UserJpaRepository userJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;
  private PatientJpaRepository patientJpaRepository;

  @Test
  void thatFindByPatientIdWorks() {
    DiagnosedDiseaseEntity diagnosesDisease = createDiagnosesDisease();

    Set<DiagnosedDiseaseEntity> result = diagnosesDiseaseJpaRepository.findByPatientId(diagnosesDisease.getPatient().getPatientId());

    Assertions.assertThat(result.contains(diagnosesDisease));
  }


  @NotNull
  private DiagnosedDiseaseEntity createDiagnosesDisease() {
    DoctorEntity doctorEntity = createDoctor();
    PatientEntity patientEntity = createPatient();

    DiagnosedDiseaseEntity build = DiagnosedDiseaseEntity.builder()
        .diagnosisDate(OffsetDateTime.now())
        .diseaseName("test")
        .description("test")
        .patient(patientEntity)
        .doctor(doctorEntity)
        .build();

    return diagnosesDiseaseJpaRepository.saveAndFlush(build);
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