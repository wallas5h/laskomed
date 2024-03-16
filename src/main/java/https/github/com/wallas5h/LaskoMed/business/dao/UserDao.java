package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;

import java.util.Optional;

public interface UserDao {
  UserEntity save(UserEntity userEntity);

  UserEntity findByUsernameOrEmail(String usernameOrEmail);

  UserEntity findByUsernameOrEmail(String username, String email);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);
}
