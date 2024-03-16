package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.domain.DoctorAvailability;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorAvailabilityMapper {
  DoctorAvailabilityDTO map(DoctorAvailability doctorAvailability);

  DoctorAvailability mapFromEntity(DoctorAvailabilityEntity entity);


  DoctorAvailabilityDTO mapFromEntityToDto(DoctorAvailabilityEntity entity);

  DoctorAvailabilityEntity mapToEntity(DoctorAvailability object);

//  mapFromAvailabilityRequestTo
}
