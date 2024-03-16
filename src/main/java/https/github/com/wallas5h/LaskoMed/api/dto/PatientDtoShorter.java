package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDtoShorter {

  private Long patientId;
  private String name;
  private String surname;
  private String pesel;
  private LocalDate birthdate;
  private String email;
  private String phone;
  private String medicalPackage;
  private String gender;

}
