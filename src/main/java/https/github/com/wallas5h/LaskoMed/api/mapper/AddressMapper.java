package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.AddressDTO;
import https.github.com.wallas5h.LaskoMed.domain.Address;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  AddressDTO map(Address address);

  @Mapping(target = "patient", ignore = true )
  @Mapping(target = "clinic", ignore = true )
  Address mapFromEntity(AddressEntity entity);

  AddressEntity mapToEntity(Address object);
}
