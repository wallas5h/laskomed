package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

  private Long addressId;
  private String country;
  private String voivodeship;
  private String postalCode;
  private String houseNumber;
  private String city;
  private String street;
  private String apartmentNumber;
  PatientDTO patient;
  ClinicDTO clinic;
}
