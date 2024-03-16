package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
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
class RoleJpaRepositoryTest {
  private RoleRepository roleRepository;

  @Test
  void thatFindRoleByNameWorks() {
    RoleEntity roleEntity = RoleEntity.builder()
        .name(EnumsContainer.RoleNames.PATIENT.name())
        .build();

    RoleEntity roleEntity1 = roleRepository.saveAndFlush(roleEntity);

    Optional<RoleEntity> byId = roleRepository.findById(roleEntity1.getRoleId());
    Assertions.assertThat(byId.isPresent());

  }

}