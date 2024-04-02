package https.github.com.wallas5h.LaskoMed.api.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
  private String username;

  @Size(min = 7, max = 32, message = "Password must be between 7 and 32 characters")
  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32}$", message = "Invalid password format")
  private String password;
  private String email;
  private String role;
}
