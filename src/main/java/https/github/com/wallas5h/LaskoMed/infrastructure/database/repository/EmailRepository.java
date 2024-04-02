package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.business.dao.EmailDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.EmailConfirmationTokenEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.EmailConfirmationTokenJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class EmailRepository implements EmailDao {
  private EmailConfirmationTokenJpaRepository emailConfirmationTokenJpaRepository;

  @Override
  public UserEntity findUserByToken(String token) {
    EmailConfirmationTokenEntity emailConfirmationTokenEntity = emailConfirmationTokenJpaRepository.findByToken(token)
        .orElseThrow(() -> new EntityNotFoundException(
            "Confirmation details not found, token: [%s]".formatted(token)
        ));
    return emailConfirmationTokenEntity.getUser();
  }
}
