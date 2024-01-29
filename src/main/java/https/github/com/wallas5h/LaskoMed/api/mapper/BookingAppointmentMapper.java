package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.BookingAppointment;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingAppointmentMapper {
  BookingAppointmentDTO map(BookingAppointment bookingAppointment);

  @Mapping(target = "patient", ignore = true )
  @Mapping(target = "doctor", ignore = true )
  @Mapping(target = "clinic", ignore = true )
  BookingAppointment mapFromEntity(BookingAppointmentEntity entity);

  BookingAppointmentEntity mapToEntity(BookingAppointment object);
}
