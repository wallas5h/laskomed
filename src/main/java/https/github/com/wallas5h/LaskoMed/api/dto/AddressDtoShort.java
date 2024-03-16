package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDtoShort {

  private Long addressId;
  private String city;
  private String street;
  private String houseNumber;
  private String apartmentNumber;

}
