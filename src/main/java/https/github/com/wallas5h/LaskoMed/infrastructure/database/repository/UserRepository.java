package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository implements UserDao {
  private UserJpaRepository userJpaRepository;

  @Override
  public UserEntity save(UserEntity userEntity) {
    return userJpaRepository.save(userEntity);
  }

  @Override
  public UserEntity findByUsernameOrEmail(String usernameOrEmail) {
    return userJpaRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
        () ->
            new UsernameNotFoundException("User not found by username or email: [%s]".formatted(usernameOrEmail)));
  }

  public UserEntity findByUsernameOrEmail(String username, String email) {
    return userJpaRepository.findByUsernameOrEmail(username, email).orElseThrow(
        () ->
            new UsernameNotFoundException("User not found by username or email: [%s], [%s]".formatted(username, email)));
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return userJpaRepository.findByUsername(username);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return userJpaRepository.findByEmail(email);
  }
}
