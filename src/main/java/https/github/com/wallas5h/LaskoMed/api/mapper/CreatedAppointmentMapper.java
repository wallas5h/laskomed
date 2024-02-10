package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.CreatedAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.domain.CreatedAppointment;
import https.github.com.wallas5h.LaskoMed.domain.DoctorAvailability;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.CreatedAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorAvailabilityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatedAppointmentMapper {
  CreatedAppointmentDTO map(CreatedAppointment createdAppointment);

  CreatedAppointment mapFromEntity(CreatedAppointmentEntity entity);

  CreatedAppointmentDTO mapFromEntityToDto(CreatedAppointmentEntity entity);

  CreatedAppointmentEntity mapToEntity(CreatedAppointment object);
}
