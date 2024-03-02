package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.*;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  private String usernameOrEmail;
  private String password;
}
