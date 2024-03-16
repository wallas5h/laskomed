package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorJpaRepositoryTest {

  private final UserJpaRepository userJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;

  @Test
  void thatFindDoctorByUserIdWorks() {
    UserEntity userEntity = userJpaRepository.saveAndFlush(EntityFixtures.someUserEntity());

    DoctorEntity doctorEntity = doctorJpaRepository
        .saveAndFlush(EntityFixtures.someDoctorEntity().withAppUser(userEntity));
    Optional<DoctorEntity> doctorEntity1 = doctorJpaRepository.findByUserId(doctorEntity.getAppUser().getUserId());
    Assertions.assertThat(doctorEntity1.isPresent());
  }

}