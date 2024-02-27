package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientCreateRequest {
  private String name;
  private String surname;
  private String pesel;
  private String birthdate;
  private String email;
  private String phone;
  private String gender;
  private String medicalPackage;
  private AddressCreateRequestDto address;
}
