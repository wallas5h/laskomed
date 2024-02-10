package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
  private Long doctorId;
  private String name;
  private String surname;
  private String pesel;
  private String specialization;
  private String PWZ_number;
  private String phone;
  private UserDtoId appUser;
}
