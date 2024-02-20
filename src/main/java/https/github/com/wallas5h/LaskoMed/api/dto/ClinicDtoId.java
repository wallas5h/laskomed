package https.github.com.wallas5h.LaskoMed.api.dto;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicDtoId {
  private Long clinicId;
  private String name;
  private AddressDtoShort address;
}
