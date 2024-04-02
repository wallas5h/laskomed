package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;

public interface EmailDao {
  UserEntity findUserByToken(String token);

}
