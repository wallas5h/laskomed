package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.AppUserDTO;
import https.github.com.wallas5h.LaskoMed.domain.AppUser;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AppUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

  AppUserDTO map(AppUser address);

  AppUserDTO mapFromEntityToDto(AppUserEntity entity);

  AppUser mapFromEntity(AppUserEntity entity);

  AppUserEntity mapToEntity(AppUser object);
}
