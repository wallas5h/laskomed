package https.github.com.wallas5h.LaskoMed.api.dto;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AppUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
  private Long roleId;
  private String name;
  private Set<AppUserDTO> users;
}
