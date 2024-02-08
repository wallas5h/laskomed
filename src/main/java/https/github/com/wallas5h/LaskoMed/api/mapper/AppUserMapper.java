package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.UserDTO;
import https.github.com.wallas5h.LaskoMed.domain.AppUser;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

  UserDTO map(AppUser address);

  UserDTO mapFromEntityToDto(UserEntity entity);

  AppUser mapFromEntity(UserEntity entity);

  UserEntity mapToEntity(AppUser object);
}
