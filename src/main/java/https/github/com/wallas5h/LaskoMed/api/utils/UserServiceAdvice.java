package https.github.com.wallas5h.LaskoMed.api.utils;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceAdvice {

  public Long getUserId() {
    UserEntity tokenUser= (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return tokenUser.getUserId();
  }
}

