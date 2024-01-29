package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.domain.DoctorAvailability;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorAvailabilityMapper {
  DoctorAvailabilityDTO map(DoctorAvailability doctorAvailability);

  @Mapping(target = "doctor", ignore = true )
  @Mapping(target = "clinic", ignore = true )
  DoctorAvailability mapFromEntity(DoctorAvailabilityEntity entity);

  DoctorAvailabilityEntity mapToEntity(DoctorAvailability object);
}
