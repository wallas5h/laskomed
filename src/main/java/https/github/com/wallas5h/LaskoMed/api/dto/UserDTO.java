package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  private Long userId;
  private String username;
  private String email;
  private Boolean enabled;
  private Boolean confirmed;
  private Set<RoleDTO> roles;
}
