package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateRequest {
  private String name;
  private String surname;
  private String pesel;
  private String specialization;
  private String PwzNumber;
  private String phone;
}
