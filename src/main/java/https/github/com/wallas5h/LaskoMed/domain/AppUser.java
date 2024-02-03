package https.github.com.wallas5h.LaskoMed.domain;

import jakarta.persistence.*;
import lombok.*;

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
}
