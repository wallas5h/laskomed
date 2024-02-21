package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.AvailableAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AvailableAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailableAppointmentDAO {
  void changeAvailableAppointmentToActive(Optional<BookingAppointmentEntity> bookingEntity);

  void save(AvailableAppointmentEntity newCreatedAppointment);

  List<AvailableAppointmentDTO> getAvailableMedicalAppointments(LocalDate date, String specialization, String location);
}
