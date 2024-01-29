package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.domain.Doctor;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  DoctorDTO map(Doctor doctor);

  Doctor mapFromEntity(DoctorEntity entity);

  DoctorEntity mapToEntity(Doctor object);
}
