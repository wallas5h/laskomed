package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.UserDTO;
import https.github.com.wallas5h.LaskoMed.domain.User;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

  UserDTO map(User address);

  UserDTO mapFromEntityToDto(UserEntity entity);

  @Mapping(target = "roles", ignore = true )
  User mapFromEntity(UserEntity entity);

  @Mapping(target = "roles", ignore = true )
  UserEntity mapToEntity(User object);
}
