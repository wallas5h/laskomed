package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.MedicalAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.MedicalAppointment;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {
  MedicalAppointmentDTO map(MedicalAppointment medicalAppointment);

  @Mapping(target = "patient", ignore = true )
  @Mapping(target = "clinic", ignore = true )
  @Mapping(target = "bookingAppointment", ignore = true )
  @Mapping(target = "doctor", ignore = true )
  MedicalAppointment mapFromEntity(MedicalAppointmentEntity entity);


  MedicalAppointmentDTO mapFromEntityToDto(MedicalAppointmentEntity entity);

  MedicalAppointmentEntity mapToEntity(MedicalAppointment object);
}
