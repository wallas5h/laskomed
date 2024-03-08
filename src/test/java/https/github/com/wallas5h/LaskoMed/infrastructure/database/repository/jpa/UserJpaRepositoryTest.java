package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

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
class UserJpaRepositoryTest {

  @Autowired
  private final UserJpaRepository userJpaRepository;

  @Test
  void thatUserCanBeFindByUsernameOrEmail(){
    UserEntity userEntity = EntityFixtures.someUserEntity();
    saveExampleUser();

    Optional<UserEntity> byUsernameOrEmail = userJpaRepository.findByUsernameOrEmail(userEntity.getUsername(), userEntity.getEmail());
    Assertions.assertThat(byUsernameOrEmail.isPresent());

  }

  @Test
  void thatUserCanBeFindByUsername(){
    UserEntity userEntity = EntityFixtures.someUserEntity();
    saveExampleUser();

    Optional<UserEntity> byUsername = userJpaRepository.findByUsername(userEntity.getUsername());
    Assertions.assertThat(byUsername.isPresent());
  }

  @Test
  void thatUserCanBeFindByEmail(){
    UserEntity userEntity = EntityFixtures.someUserEntity();
    saveExampleUser();

    Optional<UserEntity> byEmail = userJpaRepository.findByEmail(userEntity.getEmail());
    Assertions.assertThat(byEmail.isPresent());
  }

  void saveExampleUser(){
    userJpaRepository.saveAndFlush(EntityFixtures.someUserEntity());
  }

}