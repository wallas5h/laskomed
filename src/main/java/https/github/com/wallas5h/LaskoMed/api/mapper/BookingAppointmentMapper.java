package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.BookingAppointment;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingAppointmentMapper {
  BookingAppointmentDTO map(BookingAppointment bookingAppointment);

  BookingAppointmentDTO mapFromEntityToDto(BookingAppointmentEntity entity);


  BookingAppointment mapFromEntity(BookingAppointmentEntity entity);

  BookingAppointmentEntity mapToEntity(BookingAppointment object);
}
