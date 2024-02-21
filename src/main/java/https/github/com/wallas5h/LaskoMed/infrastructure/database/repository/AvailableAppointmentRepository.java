package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.api.dto.AvailableAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.AvailableAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.AvailableAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AvailableAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AvailableAppointmentRepository implements AvailableAppointmentDAO {
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private AvailableAppointmentMapper availableAppointmentMapper;

  @Override
  public void changeAvailableAppointmentToActive(Optional<BookingAppointmentEntity> bookingEntity) {
    AvailableAppointmentEntity availableAppointment = bookingEntity.get().getAvailableAppointment();
    availableAppointment.setIsActive(true);
    availableAppointmentJpaRepository.save(availableAppointment);
  }

  @Override
  public void save(AvailableAppointmentEntity newCreatedAppointment) {
    availableAppointmentJpaRepository.save(newCreatedAppointment);
  }

  @Override
  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(LocalDate date, String specialization, String location) {
    return availableAppointmentJpaRepository.getAvailableMedicalAppointments(date, specialization, location).stream()
        .map(availableAppointmentMapper::mapFromEntityToDto)
        .toList();
  }
}
