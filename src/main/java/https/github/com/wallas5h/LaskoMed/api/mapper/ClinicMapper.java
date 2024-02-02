package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.ClinicDTO;
import https.github.com.wallas5h.LaskoMed.domain.Clinic;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
  ClinicDTO map(Clinic clinic);

  @Mapping(target = "address", ignore = true )
  ClinicDTO mapFromEntityToDto(ClinicEntity entity);

  @Mapping(target = "address", ignore = true )
  Clinic mapFromEntity(ClinicEntity entity);

  ClinicEntity mapToEntity(Clinic object);
}
