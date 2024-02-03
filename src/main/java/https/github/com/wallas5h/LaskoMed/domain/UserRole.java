package https.github.com.wallas5h.LaskoMed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "appUserRoleId")
@ToString(of = {"appUserRoleId", "appUserId", "role", })
public class UserRole {

  Long appUserRoleId;
  Long appUserId;
  String role;
}
