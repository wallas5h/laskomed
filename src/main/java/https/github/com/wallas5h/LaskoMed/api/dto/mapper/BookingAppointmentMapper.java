package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.BookingAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingAppointmentMapper {
  BookingAppointmentDTO map(BookingAppointment bookingAppointment);
}
