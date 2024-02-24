package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "appUserId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user_role")
public class UserRoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "app_user_role_id")
  private Long appUserRoleId;

  @Column(name = "user_id")
  private Long appUserId;

  @Column(name = "role_id")
  private Long roleId;

}
