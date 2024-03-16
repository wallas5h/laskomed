package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.domain.Doctor;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  DoctorDTO map(Doctor doctor);

  Doctor mapFromEntity(DoctorEntity entity);

  DoctorDTO mapFromEntityToDto(DoctorEntity entity);

  DoctorEntity mapToEntity(Doctor object);

  default Long mapUserId(UserEntity appUser) {
    return appUser == null ? null : appUser.getUserId();
  }
}
