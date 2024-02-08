package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.AddressDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.RoleDTO;
import https.github.com.wallas5h.LaskoMed.domain.Address;
import https.github.com.wallas5h.LaskoMed.domain.Role;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  RoleDTO map(Role role);

  @Mapping(target = "users", ignore = true )
  RoleDTO mapFromEntityToDto(RoleEntity entity);

  @Mapping(target = "users", ignore = true )
  Role mapFromEntity(RoleEntity entity);

  RoleEntity mapToEntity(Role object);
}
