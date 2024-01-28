package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.AddressDTO;
import https.github.com.wallas5h.LaskoMed.domain.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  AddressDTO map(Address address);
}
