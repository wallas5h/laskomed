package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.AvailableAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.CreatedAppointment;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AvailableAppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvailableAppointmentMapper {
  AvailableAppointmentDTO map(CreatedAppointment createdAppointment);

  CreatedAppointment mapFromEntity(AvailableAppointmentEntity entity);

  AvailableAppointmentDTO mapFromEntityToDto(AvailableAppointmentEntity entity);

  AvailableAppointmentEntity mapToEntity(CreatedAppointment object);
}
