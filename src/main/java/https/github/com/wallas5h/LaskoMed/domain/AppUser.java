package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "appUserId")
@ToString(of = {"appUserId", "username", "password", "email", "enabled", "confirmed"  })
public class AppUser {
  Long appUserId;
  String username;
  String password;
  String email;
  Boolean enabled;
  Boolean confirmed;
  Set<RoleEntity> roles;

  public Set<RoleEntity> getRoles(){
    return Objects.isNull( roles) ? new HashSet<>() : roles;
  }
}
