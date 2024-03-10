package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

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

import java.util.Optional;
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientJpaRepositoryTest {

  private final UserJpaRepository userJpaRepository;
  private PatientJpaRepository patientJpaRepository;

  @Test
  void thatFindByUserIdWorks(){
    PatientEntity patientEntity = createPatient();
    Optional<PatientEntity> patientEntity1 = patientJpaRepository.findByUserId(patientEntity.getAppUser().getUserId());
    Assertions.assertThat(patientEntity1.isPresent());
  }

  @Test
  void thatFindByPeselWorks(){
    PatientEntity patientEntity = createPatient();
    Optional<PatientEntity> patientEntity1 = patientJpaRepository.findByPesel(patientEntity.getPesel());
    Assertions.assertThat(patientEntity1.isPresent());
  }

  @NotNull
  private PatientEntity createPatient() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserEntity());
    PatientEntity patientEntity = patientJpaRepository.saveAndFlush(
        EntityFixtures.somePatientEntity().withAppUser(userEntity));
    return patientEntity;
  }


}