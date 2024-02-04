package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AppUserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "roleId")
@ToString(of = {"roleId", "name" })
public class Role {

  Long roleId;
  String name;
  Set<AppUserEntity> users;

  public Set<AppUserEntity> getRoles(){
    return Objects.isNull( users) ? new HashSet<>() : users;
  }
}
